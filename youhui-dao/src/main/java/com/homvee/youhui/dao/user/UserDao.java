package com.homvee.youhui.dao.user;

import com.homvee.youhui.dao.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Copyright (c) 2018$. ddyunf.com all rights reserved
 *
 * @author Homvee.Tang(tanghongwei @ ddcloudf.com)
 * @version V1.0
 * @Description TODO(用一句话描述该文件做什么)
 * @date 2018-08-17 10:55
 */
public interface UserDao extends JpaRepository<User, Long>, UserDaoExt {

    /**
     * find by name & pwd
     * @param userName
     * @param pwd
     * @param yn
     * @return
     */
    User findByUserNameAndPwdAndYn(String userName, String pwd, Integer yn);

    /**
     * find by auth-key
     * @param authKey
     * @param yn
     * @return
     */
    User findByAuthKeyAndYn(String authKey, Integer yn);
}
