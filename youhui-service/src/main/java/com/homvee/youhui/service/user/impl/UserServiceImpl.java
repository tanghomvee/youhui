package com.homvee.youhui.service.user.impl;

import com.homvee.youhui.common.enums.YNEnum;
import com.homvee.youhui.common.vos.Pager;
import com.homvee.youhui.common.vos.UserVO;
import com.homvee.youhui.dao.user.UserDao;
import com.homvee.youhui.dao.user.model.User;
import com.homvee.youhui.service.BaseServiceImpl;
import com.homvee.youhui.service.user.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

/**
 * Copyright (c) 2018$. ddyunf.com all rights reserved
 *
 * @author Homvee.Tang(tanghongwei @ ddcloudf.com)
 * @version V1.0
 * @Description TODO(用一句话描述该文件做什么)
 * @date 2018-08-17 15:04
 */
@Service("userService")
public class UserServiceImpl extends BaseServiceImpl<User ,Long> implements UserService {

    @Resource
    private UserDao userDao;

    @Override
    public List<User> save(List<User> users) {
        return userDao.saveAll(users);
    }

    @Override
    public User findOne(Long id) {
        Optional<User> optional = userDao.findById(id);
        return optional != null && optional.isPresent() ? optional.get() : null;
    }

    @Override
    public Pager findByConditions(UserVO userVO, Pager pager) {
        return null;
    }

    @Override
    public User findByUserNameAndPwd(String userName, String pwd) {
        return userDao.findByUserNameAndPwdAndYn(userName , pwd , YNEnum.YES.getVal());
    }

    @Override
    public User findByAuthKey(String authKey) {
        return userDao.findByAuthKeyAndYn(authKey, YNEnum.YES.getVal());
    }
}
