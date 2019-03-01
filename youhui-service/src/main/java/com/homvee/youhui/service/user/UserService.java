package com.homvee.youhui.service.user;

import com.homvee.youhui.dao.user.model.User;

public interface UserService {

    User findByMobileAndOpenId(String mobile, String openId);

    User findByMobile(String mobile);

    boolean saveOrUpdate(User userNew);

    User findByOpenid(String openId);

    Integer findRecommenderCount(Long id);

    Integer findRewardAmtCount(Long id);
}
