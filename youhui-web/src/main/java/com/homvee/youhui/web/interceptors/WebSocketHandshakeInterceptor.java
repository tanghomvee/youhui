package com.homvee.youhui.web.interceptors;

import com.google.common.base.Strings;
import com.homvee.youhui.common.constants.SessionKey;
import com.homvee.youhui.dao.acct.model.Account;
import com.homvee.youhui.dao.room.model.Room;
import com.homvee.youhui.dao.user.model.User;
import com.homvee.youhui.service.acct.AccountService;
import com.homvee.youhui.service.room.RoomService;
import com.homvee.youhui.service.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.util.CollectionUtils;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

/**
 * Copyright (c) 2018$. ddyunf.com all rights reserved
 *
 * @author Homvee.Tang(tanghongwei @ ddcloudf.com)
 * @version V1.0
 * @Description TODO(用一句话描述该文件做什么)
 * @date 2018-09-29 09:52
 */
public class WebSocketHandshakeInterceptor extends HttpSessionHandshakeInterceptor {

    protected Logger LOGGER  = LoggerFactory.getLogger(this.getClass());

    @Resource
    private RoomService roomService;
    @Resource
    private AccountService accountService;
    @Resource
    private UserService userService;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {

        if (!super.beforeHandshake(request, response, wsHandler, attributes)){
            return false;
        }
        ServletServerHttpRequest req = (ServletServerHttpRequest) request;
        HttpSession session = req.getServletRequest().getSession();
        if (session == null) {
            LOGGER.error("请求对应的会话不存在:uri={},params={}" , req.getURI() , req.getServletRequest().getQueryString());
            return false;
        }

        Account account = (Account) session.getAttribute(SessionKey.ACCOUNT);
        if (account != null){
            return true;
        }

        String acctName = req.getServletRequest().getParameter("acctName");
        if (Strings.isNullOrEmpty(acctName)) {
            LOGGER.error("账户名错误");
            return false;
        }

        String roomUrl = req.getServletRequest().getParameter("roomUrl");
        if (Strings.isNullOrEmpty(roomUrl)) {
            LOGGER.error("房间地址错误");
            return false;
        }

        String authKey = req.getServletRequest().getParameter("authKey");
        if (Strings.isNullOrEmpty(authKey)) {
            LOGGER.error("授权错误");
            return false;
        }

        LOGGER.info("握手完成前:acctName={},roomUrl={},authKey={}" , acctName ,roomUrl,authKey);


        acctName = acctName.trim();
        User user = userService.findByAuthKey(authKey);
        if(user == null){
            LOGGER.error("未授权的用户:authKey={}" ,authKey);
            return false;
        }

        List<Account> accounts = accountService.findByAcctNameAndUserId(acctName , user.getId());
        if(CollectionUtils.isEmpty(accounts) || accounts.size() != 1){
            LOGGER.error("账户不存在:acctName={}" , acctName);
            return false;
        }

        List<Room> rooms = roomService.findByUrlAndUserId(roomUrl , user.getId());
        if(CollectionUtils.isEmpty(rooms) || rooms.size() != 1){
            LOGGER.warn("用户不存在此房间:roomUrl={},userId={}" , roomUrl , user.getId());
        }else{
            attributes.put(SessionKey.ROOM , rooms.get(0));
        }
        account = accounts.get(0);
        attributes.put(SessionKey.USER , user);
        attributes.put(SessionKey.ACCOUNT , account);
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception ex) {
        ServletServerHttpRequest serverRequest = (ServletServerHttpRequest) request;
        LOGGER.info("与账号房间握手:acctName={},roomUrl={}" , serverRequest.getServletRequest().getParameter("acctName") , serverRequest.getServletRequest().getParameter("roomUrl"));
        super.afterHandshake(request, response, wsHandler, ex);
    }
}
