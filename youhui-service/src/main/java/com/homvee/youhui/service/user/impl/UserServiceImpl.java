package com.homvee.youhui.service.user.impl;

import com.homvee.youhui.dao.user.UsrDao;
import com.homvee.youhui.dao.user.model.User;
import com.homvee.youhui.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UsrDao usrDao;

    @Override
    public User findByMobileAndOpenId(String mobile, String openId) {
        return usrDao.findByMobileAndOpenId(mobile,openId);
    }

    @Override
    public User findByMobile(String mobile) {
        return usrDao.findByMobile(mobile);
    }

    @Override
    public boolean saveOrUpdate(User userNew) {
        if(userNew.getId()==null){
            User user = usrDao.save(userNew);
            return user==null?false:true;
        }
        User user = usrDao.saveAndFlush(userNew);
        return user==null?false:true;
    }

    @Override
    public User findByOpenid(String openId) {
        return usrDao.findByOpenId(openId);
    }

    @Override
    public Integer findRecommenderCount(Long id) {
        return usrDao.findRecommenderCount(id);
    }

    @Override
    public Integer findRewardAmtCount(Long id) {
        return usrDao.findRewardAmtCount(id);
    }
}
