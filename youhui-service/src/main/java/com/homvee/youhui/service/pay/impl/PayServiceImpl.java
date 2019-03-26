package com.homvee.youhui.service.pay.impl;

import com.homvee.youhui.common.enums.SysCodeEnum;
import com.homvee.youhui.common.pay.WXPayConstants;
import com.homvee.youhui.common.pay.WXPayUtil;
import com.homvee.youhui.common.utils.HttpUtils;
import com.homvee.youhui.common.vos.Msg;
import com.homvee.youhui.dao.cfg.SysCfgDao;
import com.homvee.youhui.dao.cfg.model.SysCfg;
import com.homvee.youhui.service.pay.PayService;
import org.apache.http.Consts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service("payService")
public class PayServiceImpl implements PayService {

    private static Logger LOGGER = LoggerFactory.getLogger(PayServiceImpl.class);

    @Value("${wechat.app.id}")
    private String appId;

    @Value("${wechat.pay.mch_id}")
    private String mchId;

    @Value("${wechat.pay.notify_url}")
    private String notifyUrl;

    @Value("${wechat.pay.trade_type}")
    private String tradeType;

    @Value("${wechat.pay.sign_key}")
    private String signKey;

    @Value("${wechat.pay.url}")
    private String payUrl;


    @Autowired
    private SysCfgDao sysCfgDao;

    @Override
    public Msg findPayInfo(String openid) {
        Map<String,Object> resultMap = new HashMap<>();
        SysCfg sysCfg = sysCfgDao.findByCodeAndYn(SysCodeEnum.PAY_MONEY.getValue(), 1);
        resultMap.put("money",sysCfg.getCodeVal());
        resultMap.put("openid",openid);
        return Msg.success("获取充值金额成功",resultMap);
    }

    @Override
    public Msg createCharge(String openid) {

        SysCfg sysCfg = sysCfgDao.findByCodeAndYn(SysCodeEnum.PAY_MONEY.getValue(), 1);

        Map<String,String> paramsMap = new HashMap<>();
        paramsMap.put("appid",appId);
        paramsMap.put("mch_id",mchId);
        paramsMap.put("nonce_str", WXPayUtil.generateNonceStr());


        paramsMap.put("body","充值金额");
        paramsMap.put("out_trade_no", UUID.randomUUID().toString().replaceAll("-",""));
        paramsMap.put("total_fee",Integer.parseInt(sysCfg.getCodeVal())*100+"");
        paramsMap.put("spbill_create_ip","127.0.0.1");
        paramsMap.put("notify_url",notifyUrl);
        paramsMap.put("trade_type",tradeType);
        paramsMap.put("openid",openid);
        try {
            //paramsMap.put("sign",WXPayUtil.generateSignature(paramsMap,signKey));
            String xml = WXPayUtil.generateSignedXml(paramsMap, signKey);
            String postXML = HttpUtils.postXML(payUrl, xml, Consts.UTF_8);
        } catch (Exception e) {
            LOGGER.error("签名异常--->{}",e);
            return Msg.error("生成订单失败");
        }



        return null;
    }
}
