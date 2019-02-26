package com.homvee.youhui.service.content.impl;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.homvee.youhui.common.components.RedisComponent;
import com.homvee.youhui.common.constants.RedisKey;
import com.homvee.youhui.common.enums.SeparatorEnum;
import com.homvee.youhui.common.enums.WayEnum;
import com.homvee.youhui.common.vos.Msg;
import com.homvee.youhui.common.vos.RspBody;
import com.homvee.youhui.dao.acct.model.Account;
import com.homvee.youhui.dao.content.model.Content;
import com.homvee.youhui.dao.dictionary.model.Dictionary;
import com.homvee.youhui.dao.room.model.Room;
import com.homvee.youhui.service.acct.AccountService;
import com.homvee.youhui.service.content.AutoPushContent;
import com.homvee.youhui.service.content.ContentService;
import com.homvee.youhui.service.dictionary.DictionaryService;
import com.homvee.youhui.service.room.RoomService;
import com.homvee.youhui.service.websocket.WebSocketMsgHandler;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.TextMessage;

import javax.annotation.Resource;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * Copyright (c) 2018$. ddyunf.com all rights reserved
 *
 * @author Homvee.Tang(tanghongwei @ ddcloudf.com)
 * @version V1.0
 * @Description TODO(用一句话描述该文件做什么)
 * @date 2018-09-29 16:30
 */
@Component
public class AutoPushContentImpl  implements AutoPushContent,InitializingBean {
    protected Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    @Resource
    private ContentService contentService;
    @Resource
    private RoomService roomService;
    @Resource
    private RedisComponent redisComponent;
    @Resource
    private WebSocketMsgHandler webSocketMsgHandler;
    @Resource
    private AccountService accountService;
    @Resource
    private DictionaryService dictionaryService;


    private String[] randStrs = new String[]{
//                "^_^ "," ԅ(¯㉨¯ԅ) "," （￢㉨￢）   ","  ٩(♡㉨♡ )۶  ","  ヽ(○^㉨^)ﾉ♪ ","  (╥ ㉨ ╥`)   ","  ҉٩(*^㉨^*)  ",
//                " （≧㉨≦） "," （⊙㉨⊙） "," (๑•́ ㉨ •̀๑) "," ◟(░´㉨`░)◜ ",
//            "·","^","`",".","_","~",",","、","¯","♡","o_o","I","i","|","l"
//            "来","哈哈","呀！", "呢","好","哦","吗？","666666","go","888888","9494","关注主播,惊喜连连","送点什么","闹热","火火","🐉","🐮","㊣","哇","嘛","高","行","各位斗友,走一波打赏"
            "强","厉害","来","哈哈","呀！", "呢","好","哦","吗？","666666","go","888888","9494","关注主播,惊喜连连","送点什么","闹热","火火","哇","嘛","高","行","各位斗友,走一波打赏"
    };
    private String[] emots = new String[]{
            "[emot:dy101]", "[emot:dy102]", "[emot:dy103]", "[emot:dy104]", "[emot:dy105]", "[emot:dy106]", "[emot:dy107]", "[emot:dy108]", "[emot:dy109]",
            "[emot:dy110]", "[emot:dy111]", "[emot:dy112]", "[emot:dy113]", "[emot:dy114]", "[emot:dy115]", "[emot:dy116]", "[emot:dy117]", "[emot:dy118]",
            "[emot:dy119]", "[emot:dy120]", "[emot:dy121]", "[emot:dy122]", "[emot:dy123]", "[emot:dy124]", "[emot:dy125]", "[emot:dy126]", "[emot:dy127]",
            "[emot:dy128]", "[emot:dy129]", "[emot:dy130]", "[emot:dy131]", "[emot:dy132]", "[emot:dy133]", "[emot:dy134]", "[emot:dy135]", "[emot:dy136]",
            "[emot:dy137]", "[emot:dy001]", "[emot:dy002]", "[emot:dy003]", "[emot:dy004]", "[emot:dy005]", "[emot:dy006]", "[emot:dy007]", "[emot:dy008]",
            "[emot:dy009]", "[emot:dy010]", "[emot:dy011]", "[emot:dy012]", "[emot:dy013]", "[emot:dy014]", "[emot:dy015]", "[emot:dy016]", "[emot:dy017]"
    };

    private String[] separators =  new String[]{"㊣","🐉","💗","🐮","❀","👑","🌹","👍","👌","✏","🦐","🧜‍","🐱","🚩","🎸","🖖","👄","🐰","🎈","🎉","🎁","🔥","☀","🎵","🎶","🎼","🔫"};
    private String[] punctuations =  new String[]{".","。","!","!!","^_^","♡",".。","o_o","*","㉨","⊙","🎤","🎙","҉","……"};


    @Override
    public void afterPropertiesSet() throws Exception {

        Thread thread = new Thread(() -> {
            while (true){
                try{
                    List<Account> accounts = accountService.findAll();
                    if (CollectionUtils.isEmpty(accounts)){
                        LOGGER.info("无有效的账号");
                        continue;
                    }
                    Long speakRoomTime = 90L;
                    for (Account account : accounts){

                        /**
                         * 每个账号发言间隔30秒
                         */
                        Long period = 30L;
                        if (account.getPeriod() != null && account.getPeriod() > period){
                            period = account.getPeriod().longValue();
                        }
                        Random random = new Random();
                        period = Long.valueOf(random.nextInt(speakRoomTime.intValue() - 30 )) + period;
                        if(!redisComponent.setStrNx(RedisKey.ACCOUNT + SeparatorEnum.UNDERLINE.getVal() + account.getId() , period)){
                            LOGGER.info("账号发言时间未到:acct={}", account.getAcctName());
                            continue;
                        }

                        List<Room>  rooms = roomService.findByAcctIdAndUserId(account.getId() ,account.getUserId());
                        if (CollectionUtils.isEmpty(rooms)){
                            LOGGER.warn("当前账号未配置聊天房间:{}" , account.getAcctName() );
                            continue;
                        }
                        Integer currentHour = DateTime.now().getHourOfDay();

                        Set<String> vals = redisComponent.getZSetValByScore(account.getId().toString() , 1D ,1D);
                        String acctRoomKey = "";
                        Room speakRoom = null;
                        if (CollectionUtils.isEmpty(vals)){
                            vals = redisComponent.getZSetValByScore(account.getId().toString() , 0D ,0D);
                            if (CollectionUtils.isEmpty(vals)){
                                for (Room room : rooms){
                                    if (!WayEnum.AUTO.getVal().equals(room.getWay()) || room.getStartHour() > currentHour || room.getEndHour() < currentHour){
                                        LOGGER.info("房间未到直播时间或者非自动聊天房间:{}",room.getRoomName());
                                        continue;
                                    }
                                    acctRoomKey = account.getId().toString() + SeparatorEnum.UNDERLINE.getVal() + room.getId();
                                    if(!webSocketMsgHandler.liveAcctRoom(acctRoomKey)){
                                        LOGGER.warn("房间账号未上线:room={},acct={}" , room.getRoomName() , account.getAcctName());
                                        continue;
                                    }
                                    redisComponent.addZSet(account.getId().toString() , room.getId().toString() , 0);
                                }
                            }
                        } else {
                            for (String roomIdStr : vals){
                                acctRoomKey = account.getId().toString() + SeparatorEnum.UNDERLINE.getVal() + roomIdStr;
                                if(!StringUtils.isEmpty(redisComponent.getStrNx(acctRoomKey))){
                                    speakRoom = roomService.findOne(Long.valueOf(roomIdStr));
                                    break;
                                }
                            }
                        }

                        if (speakRoom == null){
                            vals = redisComponent.getZSetValByScore(account.getId().toString() , 0D ,0D);
                            if (!CollectionUtils.isEmpty(vals)){
                                String roomIdStr = Lists.newArrayList(vals).get(0);
                                acctRoomKey = account.getId().toString() + SeparatorEnum.UNDERLINE.getVal() + roomIdStr;
                                if (redisComponent.setStrNx(acctRoomKey , speakRoomTime)){
                                    redisComponent.addZSet(account.getId().toString() , roomIdStr , 1);
                                    speakRoom = roomService.findOne(Long.valueOf(roomIdStr));
                                }
                            }
                        }


                        if (speakRoom == null){
                            redisComponent.del(account.getId().toString());
                            LOGGER.info("账号无可聊天的房间:{}" , account.getAcctName());
                            continue;
                        }
                        List<Content> contents = contentService.findByAcctIdAndRoomId(account.getId(),speakRoom.getId());
                        if (CollectionUtils.isEmpty(contents)){
                            LOGGER.info("账号未配置在该房间发言:acct={},room={}" , account.getAcctName(),speakRoom.getRoomName());
                            continue;
                        }

                        String contentStr =  getContent(Strings.nullToEmpty(contents.get(0).getContent()),account.getId() ,speakRoom);
                        RspBody rspBody = RspBody.initChatBody(contentStr);

                        TextMessage respMsg = new TextMessage(JSONObject.toJSONString(Msg.success(rspBody)));
                        webSocketMsgHandler.sendMsg2User(acctRoomKey ,respMsg);

                    }

                }catch (Exception ex){
                    LOGGER.error("推送数据异常" , ex);
                }

            }
        });
        thread.start();
    }




    private String getContent(String txt, Long acctId , Room room){
        String contentKey = "" , acctKey =  RedisKey.ACCOUNT + SeparatorEnum.MIDDLE_LINE.getVal() + acctId ;
        Long fiveMinutes = 300L;
        Integer maxCnt = 50;
        if (!StringUtils.isEmpty(txt)){
            contentKey = acctKey  + SeparatorEnum.UNDERLINE.getVal() + txt;
            if (!redisComponent.setStrNx(contentKey , fiveMinutes * 12 * 2)){
                LOGGER.warn("房间账号发言内容重复:room={},acctId={},content={}" , room.getRoomName() , acctId , txt);
                txt = "";
            }
        }
        //循环获取账号在当前房间的弹幕
        Random random = new Random();
        int num = random.nextInt(3);
        if (num < 1){
            num = 1;
        }
        List<String> contents = redisComponent.popSet(RedisKey.DICTIONARY , Long.valueOf(num));
        if (CollectionUtils.isEmpty(contents)){
            initDictionary2Redis();
            contents = redisComponent.popSet(RedisKey.DICTIONARY , Long.valueOf(num));
        }

        for (String dic : contents){
             String tmpContent = txt + separators[random.nextInt(separators.length)] + dic;
             if(tmpContent.length() > maxCnt){
                 return  txt;
             }
             txt = tmpContent;
        }
        if (num == 1 || StringUtils.isEmpty(txt)){
            txt = txt + getRandomEmotion(3);
        }
        return txt;
    }

//    private String getRandomStr(int num , String defaultContent){
//        String[] data = randStrs;
//        if (!StringUtils.isEmpty(defaultContent)){
//            String[] customerData = defaultContent.split(SeparatorEnum.COMMA.getVal());
//            if (!ArrayUtils.isEmpty(customerData)){
//                data = ArrayUtils.addAll(data , customerData);
//            }
//        }
//        num = data.length > num ? num : data.length;
//        Set<String> contents = Sets.newHashSet();
//        String rs = "";
//        Random random = new Random();
//        while (num > 0){
//
//            String content = data[random.nextInt(data.length)];
//            if (contents.contains(content)){
//                continue;
//            }
//            num--;
//            contents.add(content);
//            String tmpResult = rs + content + separators[random.nextInt(separators.length)];
//            if (tmpResult.length() >= 50){
//                return  tmpResult;
//            }
//            rs = tmpResult;
//        }
//
//        return rs + punctuations[random.nextInt(punctuations.length)];
//    }
    private String getRandomEmotion(int num){
        String rs = "";
        Set<String> emotsTmp = Sets.newHashSet();
        Random random = new Random();
        while (num > 0){
            String emot = emots[random.nextInt(emots.length)];
            if (emotsTmp.contains(emot)){
                continue;
            }
            emotsTmp.add(emot);
            rs = rs + emot;
            num--;
        }

        return rs;
    }

    /**
     * 将所有字典数据初始化到redis中
     * @return
     */
    private synchronized boolean initDictionary2Redis(){

        List<Dictionary> dictionaries = dictionaryService.findAll();
        if (CollectionUtils.isEmpty(dictionaries)){
            return false;
        }

        for (Dictionary dictionary : dictionaries){
            redisComponent.addSet(RedisKey.DICTIONARY , dictionary.getContent());
        }

        return  true;
    }
}
