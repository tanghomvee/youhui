package com.homvee.youhui.web.interceptors;

import com.alibaba.fastjson.JSON;
import com.homvee.youhui.common.constants.SessionKey;
import com.homvee.youhui.common.vos.Msg;
import com.homvee.youhui.dao.user.model.User;
import com.homvee.youhui.service.user.UserService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

public class UsrLoginInterceptor extends HandlerInterceptorAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(UsrLoginInterceptor.class);

    private static final String MEDIA_JSON = "application/json;charset=UTF-8";

    @Autowired
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        /*LOGGER.info("来自请求方的IP:remoteAddr={},remoteHost={},remotePort={},forwarded-for={}" ,
                request.getRemoteAddr() ,
                request.getRemoteHost(),
                request.getRemotePort(),
                request.getHeader("X-FORWARDED-FOR"));*/

        response.setContentType(MEDIA_JSON);
        String openid = request.getParameter("openid");
        User user = userService.findByOpenid(openid);
        if(user==null){
            String msg = JSON.toJSONString(Msg.login());
            LOGGER.info("当前用户未登录，请先登录");
            PrintWriter printWriter = response.getWriter();
            printWriter.write(msg);
            printWriter.flush();
            printWriter.close();
            return false;
        }



        return super.preHandle(request, response, handler);

    }

}
