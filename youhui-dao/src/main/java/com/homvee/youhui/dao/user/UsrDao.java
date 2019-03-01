package com.homvee.youhui.dao.user;

import com.homvee.youhui.dao.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UsrDao extends JpaRepository<User, Long> {

    User findByMobileAndOpenId(String mobile,String openId);

    User findByMobile(String mobile);

    User findByOpenId(String openId);

    @Query(value = "SELECT COUNT(1) FROM `user` WHERE recommender=?1" , nativeQuery = true)
    Integer findRecommenderCount(Long id);

    @Query(value = "SELECT COUNT(userId) AS ct FROM `charge` WHERE userId IN(SELECT id FROM `user` WHERE recommender=?1) GROUP BY userId" , nativeQuery = true)
    Integer findRewardAmtCount(Long id);
}
