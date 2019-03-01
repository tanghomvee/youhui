package com.homvee.youhui.web.ctrls;

import com.homvee.youhui.common.vos.Msg;
import com.homvee.youhui.dao.user.model.User;
import com.homvee.youhui.service.user.UserService;
import com.homvee.youhui.web.BaseCtrl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/youhui/user")
public class UserCtr extends BaseCtrl {


    @Autowired
    private UserService userService;

    /**
     * 个人中心
     */
    @RequestMapping(path = {"/myCenter"} , method = {RequestMethod.GET})
    @ResponseBody
    public Msg myCenter(String openid){

        Map<String,Object> resultMap = new HashMap<>();

        User user = userService.findByOpenid(openid);
        //奖励金额
        resultMap.put("rewardAmt",user.getRewardAmt()==null?0:user.getRewardAmt());
        //邀请人数
        Integer count = userService.findRecommenderCount(user.getId());
        resultMap.put("recommenderCount",count==null?0:count);
        //充值人数
        Integer rewardCount =  userService.findRewardAmtCount(user.getId());
        resultMap.put("rewardCount",rewardCount==null?0:rewardCount);
        return Msg.success(resultMap);
    }
}
