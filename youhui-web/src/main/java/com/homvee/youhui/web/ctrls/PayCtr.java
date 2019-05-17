package com.homvee.youhui.web.ctrls;

import com.homvee.youhui.common.pay.WXPayUtil;
import com.homvee.youhui.common.vos.Msg;
import com.homvee.youhui.service.pay.PayService;
import com.homvee.youhui.web.BaseCtrl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Map;


/**
 * 支付模块
 */
@Controller
@RequestMapping("/youhui/pay")
public class PayCtr extends BaseCtrl {

    private static Logger LOGGER = LoggerFactory.getLogger(PayCtr.class);

    @Autowired
    private PayService payService;

    @Value("${wechat.pay.sign_key}")
    private String signKey;

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
     * @return
     */
    @RequestMapping(path = {"/callBack"} , method = {RequestMethod.POST ,RequestMethod.GET})
    @ResponseBody
    public String callBack(HttpServletRequest request, HttpServletResponse response){
        //System.out.println("微信支付成功,微信发送的callback信息,请注意修改订单信息");

        try {
            InputStream is = request.getInputStream();
            Map<String, String> notifyMap = WXPayUtil.xmlToMap(inputStream2String(is));
            boolean signatureValid = WXPayUtil.isSignatureValid(notifyMap,signKey);
            if(!signatureValid){
                LOGGER.info("签名数据{}验证失败-->",notifyMap);
                return null;
            }
            //告诉微信服务器收到信息了，不要在调用回调action了========这里很重要回复微信服务器信息用流发送一个xml即可
            LOGGER.info("支付回调返回信息-->{}",notifyMap);
            payService.callBack(notifyMap);

            response.getWriter().write("<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>");
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    public static String inputStream2String(InputStream in) {
        InputStreamReader reader = null;
        try {
            reader = new InputStreamReader(in, "UTF-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        BufferedReader br = new BufferedReader(reader);
        StringBuilder sb = new StringBuilder();
        String line = "";
        try {
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

}
