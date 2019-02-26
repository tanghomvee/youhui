package com.homvee.youhui.web.ctrls;

import com.alibaba.fastjson.JSONObject;
import com.homvee.youhui.common.vos.Msg;
import com.homvee.youhui.dao.acct.model.Account;
import com.homvee.youhui.dao.sms.model.PortInfo;
import com.homvee.youhui.dao.sms.model.SendingSMS;
import com.homvee.youhui.dao.user.model.User;
import com.homvee.youhui.service.acct.AccountService;
import com.homvee.youhui.service.sms.PortInfoService;
import com.homvee.youhui.service.sms.SendingSMSService;
import com.homvee.youhui.service.user.UserService;
import com.homvee.youhui.web.BaseCtrl;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping(path = "/sms")
public class SMSCtrl extends BaseCtrl {

    @Resource
    private AccountService accountService;
    @Resource
    private SendingSMSService sendingSMSService;

    @Resource
    private PortInfoService portInfoService;

    @Resource
    private UserService userService;

    @RequestMapping(path = {"/check"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Msg checkMsg(String acctName, String authKey , String toPhone , String smsContent){
        if(StringUtils.isEmpty(acctName) || StringUtils.isEmpty(authKey) || StringUtils.isEmpty(toPhone)){
            return Msg.error("参数错误");
        }

        User user = userService.findByAuthKey(authKey);
        if(user == null){
            return Msg.error("非法账户");
        }
        List<Account> accts = accountService.findByAcctNameAndUserId(acctName.trim() , user.getId());
        if (CollectionUtils.isEmpty(accts)){
            return Msg.error("账号不存在");
        }
        JSONObject retJSON = new JSONObject();
        retJSON.put("operate" , "check");
        retJSON.put("content" , true);

        Account acct = accts.get(0);
        String mobile = acct.getMobile();
        PortInfo portInfo = portInfoService.findByPhonNum(mobile);
        SendingSMS sendingSMS = new SendingSMS();
        sendingSMS.setPhoNum(mobile);
        sendingSMS.setPortNum(portInfo.getPortNum());
        sendingSMS.setSmsContent(smsContent);
        sendingSMS.setSmsNumber(toPhone);
        sendingSMS.setSmsType(0);
        sendingSMS = sendingSMSService.save(sendingSMS, 30 * 60L);
        return Msg.success(retJSON);
    }

}
