package com.homvee.youhui.web.ctrls;

import com.alibaba.fastjson.JSONObject;
import com.homvee.youhui.common.enums.SysCodeEnum;
import com.homvee.youhui.common.utils.HttpUtils;
import com.homvee.youhui.common.vos.Msg;
import com.homvee.youhui.common.vos.TokenBean;
import com.homvee.youhui.dao.cfg.model.SysCfg;
import com.homvee.youhui.dao.user.model.User;
import com.homvee.youhui.service.sycCfg.SysCfService;
import com.homvee.youhui.service.user.UserService;
import com.homvee.youhui.service.wechat.WeChatService;
import com.homvee.youhui.web.BaseCtrl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

import static com.homvee.youhui.common.constants.WeChatKey.ERR_KEY;

@Controller
@RequestMapping("/youhui/user")
public class UserCtr extends BaseCtrl {


    @Autowired
    private UserService userService;


    @Autowired
    private WeChatService weChatService;

    @Autowired
    private SysCfService sysCfService;
    /**
     * 个人中心
     */
    @RequestMapping(path = {"/myCenter"} , method = {RequestMethod.GET})
    @ResponseBody
    public Msg myCenter(String openid){

        Map<String,Object> resultMap = new HashMap<>();

        User user = userService.findByOpenid(openid);

        Map<String,Object> userInfo = weChatService.getUserInfo(openid);

        resultMap.put("nickname",userInfo.get("nickname"));
        resultMap.put("sex",userInfo.get("sex"));
        resultMap.put("language",userInfo.get("language"));
        resultMap.put("city",userInfo.get("city"));
        resultMap.put("province",userInfo.get("province"));
        resultMap.put("country",userInfo.get("country"));
        resultMap.put("headimgurl",userInfo.get("headimgurl"));

        //手机号
        resultMap.put("mobile",user.getMobile());
        resultMap.put("activated",user.getActivated());
        //奖励金额
        resultMap.put("rewardAmt",user.getRewardAmt()==null?0:user.getRewardAmt());
        //邀请人数
        Integer count = userService.findRecommenderCount(user.getId());
        resultMap.put("recommenderCount",count==null?0:count);
        //充值人数
        Integer rewardCount =  userService.findRewardAmtCount(user.getId());
        resultMap.put("rewardCount",rewardCount==null?0:rewardCount);

        SysCfg sysCfgLogo = sysCfService.findByCodeAndYn(SysCodeEnum.FENH_LOGO.getValue(), 1);
        SysCfg sysCfgTitle = sysCfService.findByCodeAndYn(SysCodeEnum.FENH_TITLE.getValue(), 1);
        resultMap.put("logo",sysCfgLogo.getCodeVal());
        resultMap.put("title",sysCfgTitle.getCodeVal());


        //
        return Msg.success(resultMap);
    }
}
