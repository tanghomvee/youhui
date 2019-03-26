package com.homvee.youhui.web.ctrls;

import com.homvee.youhui.common.vos.Msg;
import com.homvee.youhui.service.pay.PayService;
import com.homvee.youhui.web.BaseCtrl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * 支付模块
 */
@Controller
@RequestMapping("/youhui/pay")
public class PayCtr extends BaseCtrl {

    private static Logger LOGGER = LoggerFactory.getLogger(PayCtr.class);

    @Autowired
    private PayService payService;

    @RequestMapping(path = {"/payInfo"} , method = {RequestMethod.POST ,RequestMethod.GET})
    @ResponseBody
    public Msg payInfo(String openid){
        return payService.findPayInfo(openid);
    }

    /**
     * 同一下发接口
     * @param openid
     * @return
     */
    @RequestMapping(path = {"/createCharge"} , method = {RequestMethod.POST ,RequestMethod.GET})
    @ResponseBody
    public Msg createCharge(String openid){
        return payService.createCharge(openid);
    }



    /**
     * 支付通知回调接口
     * @param openid
     * @return
     */
    @RequestMapping(path = {"/callBack"} , method = {RequestMethod.POST ,RequestMethod.GET})
    @ResponseBody
    public Msg callBack(String openid){
        return payService.createCharge(openid);
    }


}
