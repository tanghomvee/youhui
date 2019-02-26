package com.homvee.youhui.web.interceptors;

import com.alibaba.fastjson.JSON;
import com.homvee.youhui.common.constants.SessionKey;
import com.homvee.youhui.common.vos.Msg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

public class UsrLoginInterceptor extends HandlerInterceptorAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(UsrLoginInterceptor.class);

    private static final String MEDIA_JSON = "application/json;charset=UTF-8";


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        LOGGER.info("来自请求方的IP:remoteAddr={},remoteHost={},remotePort={},forwarded-for={}" ,
                request.getRemoteAddr() ,
                request.getRemoteHost(),
                request.getRemotePort(),
                request.getHeader("X-FORWARDED-FOR"));

        String uri = request.getServletPath();
        Object operator = request.getSession().getAttribute(SessionKey.USER);

        if (operator == null) {
            response.setContentType(MEDIA_JSON);
            String msg = JSON.toJSONString(Msg.login());
            LOGGER.info("请求路径{}, 当前用户未登录，请先登录", uri);
            PrintWriter printWriter = response.getWriter();
            printWriter.write(msg);
            printWriter.flush();
            printWriter.close();
            return false;
        }
        return super.preHandle(request, response, handler);

    }

}
