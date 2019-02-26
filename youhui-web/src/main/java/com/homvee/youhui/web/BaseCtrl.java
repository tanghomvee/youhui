package com.homvee.youhui.web;

import com.homvee.youhui.common.constants.SessionKey;
import com.homvee.youhui.common.vos.UserVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

/**
 * Copyright (c) 2018$. ddyunf.com all rights reserved
 *
 * @author Homvee.Tang(tanghongwei @ ddcloudf.com)
 * @version V1.0
 * @Description TODO(用一句话描述该文件做什么)
 * @date 2018-08-16 09:34
 */
public class BaseCtrl {
    protected Logger LOGGER = null;

    @Resource
    protected HttpSession session;




    public BaseCtrl() {
        LOGGER = LoggerFactory.getLogger(this.getClass());
    }


    protected UserVO getUser() {

        return (UserVO) session.getAttribute(SessionKey.USER);

    }

    protected void setUser(UserVO user) {
      session.setAttribute(SessionKey.USER , user);
    }

}
