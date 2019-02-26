package com.homvee.youhui.service.websocket;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.homvee.youhui.common.constants.SessionKey;
import com.homvee.youhui.common.enums.OperatorEnum;
import com.homvee.youhui.common.enums.SeparatorEnum;
import com.homvee.youhui.common.vos.Msg;
import com.homvee.youhui.common.vos.ReqBody;
import com.homvee.youhui.common.vos.RspBody;
import com.homvee.youhui.dao.acct.model.Account;
import com.homvee.youhui.dao.room.model.Room;
import com.homvee.youhui.dao.sms.model.PortInfo;
import com.homvee.youhui.dao.sms.model.SendingSMS;
import com.homvee.youhui.dao.user.model.User;
import com.homvee.youhui.service.content.ContentService;
import com.homvee.youhui.service.room.RoomService;
import com.homvee.youhui.service.sms.PortInfoService;
import com.homvee.youhui.service.sms.SendingSMSService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import javax.annotation.Resource;
import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

/**
 * Copyright (c) 2018$. ddyunf.com all rights reserved
 *
 * @author Homvee.Tang(tanghongwei @ ddcloudf.com)
 * @version V1.0
 * @Description web socket 消息处理类
 * @date 2018-09-29 09:48
 */
@Component("socketMsgHandler")
public class WebSocketMsgHandler extends TextWebSocketHandler {

    protected Logger LOGGER  = LoggerFactory.getLogger(this.getClass());

    private Map<String , WebSocketSession> sessions = Maps.newConcurrentMap();


    @Resource
    private SendingSMSService sendingSMSService;
    @Resource
    private PortInfoService portInfoService;

    @Resource
    private RoomService roomService;

    @Resource
    private ContentService contentService;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
        Map<String , Object> attrs = session.getAttributes();
        LOGGER.info("连接建立后处理方法:{}", attrs);

        Msg msg = getAcctRoomKey(session);
        if (!msg.isSuccess()){
            return;
        }
        String acctRoomKey = (String) msg.getData();
        WebSocketSession tmpSession = null;
        Room room = (Room) attrs.get(SessionKey.ROOM);
        //如果房间与账号不匹配通知切换直播间
        if (StringUtils.isEmpty(acctRoomKey)){
            //暂时考虑 通知会话切换房间
            tmpSession = session;
        }else{
            //如果直播房间与发言账号已经存在,让已存在的直播间切换到新的其他直播间
            tmpSession = sessions.get(acctRoomKey);
            room = null;
            if (tmpSession != null){
                LOGGER.info("账号已存在于直播间,将此账号切换到其他房间聊天:{}" , acctRoomKey);
            }
        }
        if (tmpSession != null){
            Account account = (Account) attrs.get(SessionKey.ACCOUNT);
            User user = (User) attrs.get(SessionKey.USER);
            msg = this.checkRoom(account , room ,user.getId());
            TextMessage respMsg = new TextMessage(JSONObject.toJSONString(msg));
            if (tmpSession.isOpen()){
                tmpSession.sendMessage(respMsg);
            }
            return;
        }



        sessions.put(acctRoomKey , session);
        LOGGER.info("添加账户房间会话:{}" ,acctRoomKey);
    }


    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        LOGGER.info("处理前台推送消息{}" , message.getPayload());
        Map<String , Object> attrs = session.getAttributes();
        if (CollectionUtils.isEmpty(attrs)){
            LOGGER.error("非法客户推送的消息:{}" , message.getPayload());
            return;
        }
        User user = (User) attrs.get(SessionKey.USER);
        if (user == null){
            LOGGER.error("非法用户推送的消息:{}" , message.getPayload());
            return;
        }
        Long userId = user.getId();
        Account account = (Account) attrs.get(SessionKey.ACCOUNT);
        if(account == null){
            LOGGER.error("账号信息不存在");
            return ;
        }
        Room room = (Room) attrs.get(SessionKey.ROOM);
        if(room == null){
            LOGGER.error("直播间信息不存在");
            return ;
        }

        JSONObject jsonData = JSONObject.parseObject(message.getPayload());
        ReqBody reqBody = JSONObject.toJavaObject(jsonData , ReqBody.class);
        String acctName = reqBody.getAcctName();
        //发送验证短信
        if (OperatorEnum.SMS_CHECK.getVal().equals(reqBody.getOperate())){

            String sms = reqBody.getCheckSMS().getContent();
            String toPhone = reqBody.getCheckSMS().getToPhone();

            if (StringUtils.isEmpty(sms) || StringUtils.isEmpty(toPhone) || StringUtils.isEmpty(acctName)){
                LOGGER.error("发送验证短信参数错误:sms={},toPhone={},acctName={}" , sms ,toPhone ,acctName);
                return;
            }
            Msg msg = this.sendCheckMsg(account,toPhone,sms);
            TextMessage respMsg = new TextMessage(JSONObject.toJSONString(msg));
            session.sendMessage(respMsg);
        }else if (OperatorEnum.ROOM_CHECK.getVal().equals(reqBody.getOperate())){
            String roomUrl = reqBody.getCheckRoom().getRoomUrl();
            if (StringUtils.isEmpty(roomUrl)  || StringUtils.isEmpty(acctName)){
                LOGGER.error("房间验证短信参数错误:roomUrl={},acctName={}" , roomUrl ,acctName);
                return;
            }
            Msg msg = this.checkRoom(account , room ,userId);
            TextMessage respMsg = new TextMessage(JSONObject.toJSONString(msg));
            session.sendMessage(respMsg);
        }else if (OperatorEnum.HEART_CHECK.getVal().equals(reqBody.getOperate())){
            RspBody rspBody = RspBody.initHeartbeatCheckBody();
            TextMessage respMsg = new TextMessage(JSONObject.toJSONString(Msg.success(rspBody)));
            session.sendMessage(respMsg);
        }

        super.handleTextMessage(session, message);
    }



    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        LOGGER.info("抛出异常时处理方法:{}" , getAcctRoomKey(session) ,exception);
        super.handleTransportError(session, exception);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        LOGGER.info("连接关闭后处理方法:{}" ,status);
        super.afterConnectionClosed(session, status);
        Msg msg = getAcctRoomKey(session);
        if (!msg.isSuccess()){
            return;
        }
        String acctRoomKey = (String) msg.getData();
        if (StringUtils.isEmpty(acctRoomKey)){
            return;
        }
        sessions.remove(acctRoomKey);
        LOGGER.info("移除账户房间会话:{}" ,acctRoomKey);
    }


    /**
     * 给某个用户发送消息
     *
     * @param acctRoomKey
     * @param message
     */
    public void sendMsg2User(String acctRoomKey, TextMessage message) {
        WebSocketSession user = sessions.get(acctRoomKey);
        try {
            if (user == null || !user.isOpen()) {
                LOGGER.info("房间已经断开:{}" , acctRoomKey);
                sessions.remove(acctRoomKey);
                return;
            }
            user.sendMessage(message);
            LOGGER.info("向账户房间推送消息成功:acctRoom={},msg={}" , acctRoomKey , message.getPayload());
        } catch (Exception e) {
            LOGGER.error("房间发送消息异常:{}" , acctRoomKey , e);
        }
    }
    /**
     * 房间账号是否在线
     * @param acctRoomKey
     */
    public boolean liveAcctRoom(String acctRoomKey) {
        WebSocketSession user = sessions.get(acctRoomKey);
        try {
            if (user == null || !user.isOpen()) {
                LOGGER.info("房间已经断开:{}" , acctRoomKey);
                sessions.remove(acctRoomKey);
                return false;
            }

            return true;
        } catch (Exception e) {
            LOGGER.error("房间发送消息异常:{}" , acctRoomKey , e);
        }
        return false;
    }
    /**
     * 给所有用户发送消息
     *
     * @param message
     */
    public void sendMsg2AllUser(TextMessage message) {
        try {
            if (CollectionUtils.isEmpty(sessions)){
                LOGGER.warn("无用户在任何直播间:{}" , message);
                return;
            }
            for (String acctRoomKey : sessions.keySet()){
                this.sendMsg2User(acctRoomKey ,message);
            }
        } catch (Exception e) {
            LOGGER.error("所有房间发送消息异常" ,  e);
        }
    }




    private Msg sendCheckMsg(Account acct,  String toPhone , String smsContent){


        String mobile = acct.getMobile();
        PortInfo portInfo = portInfoService.findByPhonNum(mobile);
        SendingSMS sendingSMS = new SendingSMS();
        sendingSMS.setPhoNum(mobile);
        sendingSMS.setPortNum(portInfo.getPortNum());
        sendingSMS.setSmsContent(smsContent);
        sendingSMS.setSmsNumber(toPhone);
        sendingSMS.setSmsType(0);
        sendingSMS = sendingSMSService.save(sendingSMS, 30 * 60L);
        RspBody rspBody = RspBody.initSMSCheckBody(true);
        return Msg.success(rspBody);
    }
    private Msg checkRoom(Account acct,  Room room , Long userId){

        List<Room> rooms = roomService.findByAcctIdAndUserId(acct.getId() , userId);
        if (CollectionUtils.isEmpty(rooms)){
            LOGGER.warn("此账号下无参与房间:acct={},roomId={}" , acct.getAcctName() , room.getRoomName());
            return Msg.error();
        }

        if (room != null){
            for (Room tmpRoom : rooms){
                if (tmpRoom.getId().equals(room.getId())){
                    return Msg.success(RspBody.initRoomCheckBody(tmpRoom.getUrl()));
                }
            }
        }
        for (Room tmpRoom : rooms){
            String acctRoomKey = acct.getId() + SeparatorEnum.UNDERLINE.getVal() + tmpRoom.getId();
            if (!sessions.containsKey(acctRoomKey)){
                LOGGER.warn("通知此账号下参与房间:acct={},roomId={}" , acct.getAcctName() , tmpRoom.getRoomName());
                return Msg.success(RspBody.initRoomCheckBody(tmpRoom.getUrl()));
            }
        }

        return Msg.error();
    }

    private Msg getAcctRoomKey(WebSocketSession session) throws IOException {
        Map<String , Object> attrs = session.getAttributes();
        if(CollectionUtils.isEmpty(attrs)){
            LOGGER.error("连接成功获取账号信息失败");
            return Msg.error();
        }
        Account account = (Account) attrs.get(SessionKey.ACCOUNT);
        if(account == null){
            LOGGER.error("连接成功获取账号信息不存在");
            return Msg.error();
        }
        Room room = (Room) attrs.get(SessionKey.ROOM);
        if(room == null){
            LOGGER.error("连接成功直播间信息不存在");
            User user = (User) attrs.get(SessionKey.USER);
            Msg msg = this.checkRoom(account , null ,user.getId());
            TextMessage respMsg = new TextMessage(JSONObject.toJSONString(msg));
            session.sendMessage(respMsg);
            return Msg.error();
        }
        //判断房间和账户是否对应
        List<BigInteger> accts = contentService.findAcctByRoomId(room.getId());
        Object acctRoomKey = null;
        if(!CollectionUtils.isEmpty(accts) && accts.contains(BigInteger.valueOf(account.getId()))){
            acctRoomKey = account.getId().toString() + SeparatorEnum.UNDERLINE.getVal() + room.getId();
        }

        return Msg.success(acctRoomKey);
    }
}
