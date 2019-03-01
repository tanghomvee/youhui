package com.homvee.youhui.web.ctrls;


import com.homvee.youhui.common.utils.VerifyUtil;
import com.homvee.youhui.common.vos.Msg;
import com.homvee.youhui.dao.user.model.User;
import com.homvee.youhui.service.user.UserService;
import com.homvee.youhui.service.wechat.WeChatService;
import com.homvee.youhui.web.BaseCtrl;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.util.Date;

@Controller
@RequestMapping("/youhui/wechat")
public class WeChatCtrl extends BaseCtrl {

    private static Logger LOGGER = LoggerFactory.getLogger(WeChatCtrl.class);

    @Autowired
    private WeChatService weChatService;

    @Autowired
    private UserService userService;

    @RequestMapping(path = {"/index"} , method = {RequestMethod.GET})
    @ResponseBody
    public String callback(String signature , String timestamp ,String nonce ,String echostr){
        String ret = weChatService.doCallback(signature ,  timestamp , nonce , "");
        return echostr;
    }


    /**
     * 下发验证码
     * @return
     */
    @RequestMapping(path = {"/sendPhoneVerify"} , method = {RequestMethod.POST ,RequestMethod.GET})
    @ResponseBody
    public Msg sendPhoneVerify(String phoneNo,HttpServletRequest request){
        if(StringUtils.isEmpty(phoneNo)){
            return Msg.error("手机号不能为空");
        }else if(!VerifyUtil.isMobileNO(phoneNo)){
            return Msg.error("手机号格式不正确");
        }

        String code = RandomStringUtils.randomNumeric(4);
        LOGGER.info("手机号为{}的用户的验证码为{}", phoneNo, code);

        request.getSession().setAttribute("code", code);

        LOGGER.info("向用户 {} 下发验证 ", phoneNo);
        //msgRpcService.sendMsgToMq(phoneNo,MsgTplCodeEnum.PHONE_VERIFY2.getValue(),new String[]{code},null);
        return Msg.success("发送短信成功");
    }


    /**
     * 绑定手机号
     * @param openid
     * @param phoneNo
     * @param code
     * @param invitCode
     * @param session
     * @return
     */
    @RequestMapping(path = {"/bind"} , method = {RequestMethod.POST ,RequestMethod.GET})
    @ResponseBody
    public Msg bindCourierAcct(String openid , String phoneNo , String code,String invitCode,HttpSession session){

        if(StringUtils.isEmpty(openid)){
            return Msg.error("请传绑定参数openid");
        }
        if(StringUtils.isEmpty(phoneNo) ||StringUtils.isEmpty(code) ){
            return Msg.error("请输入正确的手机号和验证码");
        }
        if(!VerifyUtil.isMobileNO(phoneNo)){
            return Msg.error("请输入正确手机号");
        }
        if(!code.equals(session.getAttribute("code"))){
            return Msg.error("验证码输入错误");
        }

        User user = userService.findByMobile(phoneNo);

        //注册用户
        if(user == null){
           User userNew = new User();
           userNew.setActivated(0);
           userNew.setMobile(phoneNo);
           userNew.setPwd("123456");
           userNew.setRewardAmt(0d);
           if(StringUtils.isBlank(invitCode)){
               userNew.setRecommender(null);
           }
           User invitUser = userService.findByMobile(invitCode);
           userNew.setRecommender(invitUser.getId());
           userNew.setOpenId(openid);
           userNew.setCreator("注册");
           userNew.setChangeTime(new Date());
           userService.saveOrUpdate(userNew);
           return Msg.success("绑定手机号成功");
        }

        //同一手机二次绑定
        if(openid.equals(user.getOpenId())){
           return  Msg.error("改手机已经绑定");
        }else{
            //解绑之后再次绑定
            //换手机绑定
            user.setOpenId(openid);
            user.setChangeTime(new Date());
            userService.saveOrUpdate(user);
            return Msg.success("绑定手机号成功");
        }
    }


    /**
     * 获取openId
     * @param code
     * @param state
     * @return
     */
    @RequestMapping(path = {"/openid"} , method = {RequestMethod.POST ,RequestMethod.GET})
    @ResponseBody
    public Msg getUsrOpenId(String code , String state){
        if(StringUtils.isEmpty(code)){
            return Msg.error("参数错误");
        }
        return weChatService.getUsrOpenId(code , state);
    }

}
