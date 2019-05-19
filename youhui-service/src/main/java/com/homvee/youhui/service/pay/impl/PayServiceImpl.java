package com.homvee.youhui.service.pay.impl;

import com.homvee.youhui.common.enums.SysCodeEnum;
import com.homvee.youhui.common.pay.WXPayConstants;
import com.homvee.youhui.common.pay.WXPayUtil;
import com.homvee.youhui.common.utils.HttpUtils;
import com.homvee.youhui.common.vos.Msg;
import com.homvee.youhui.dao.cfg.SysCfgDao;
import com.homvee.youhui.dao.cfg.model.SysCfg;
import com.homvee.youhui.dao.pay.ChargeDao;
import com.homvee.youhui.dao.pay.PayInfoDao;
import com.homvee.youhui.dao.pay.RewardDao;
import com.homvee.youhui.dao.pay.model.Charge;
import com.homvee.youhui.dao.pay.model.PayInfo;
import com.homvee.youhui.dao.pay.model.Reward;
import com.homvee.youhui.dao.user.UsrDao;
import com.homvee.youhui.dao.user.model.User;
import com.homvee.youhui.service.pay.PayService;
import org.apache.http.Consts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.net.InetAddress;
import java.util.Date;
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

    @Autowired
    private PayInfoDao payInfoDao;

    @Autowired
    private UsrDao usrDao;

    @Autowired
    private ChargeDao chargeDao;

    @Autowired
    private RewardDao rewardDao;

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
        String addr = "127.0.0.1";
        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            addr=inetAddress.getHostAddress().toString(); //获取本机ip
        }catch (Exception e){
            LOGGER.error("获取本机ip出错--->{}",e);
        }

        paramsMap.put("spbill_create_ip",addr);
        paramsMap.put("notify_url",notifyUrl);
        paramsMap.put("trade_type",tradeType);
        paramsMap.put("openid",openid);
        try {
            String xml = WXPayUtil.generateSignedXml(paramsMap, signKey);
            String postXML = HttpUtils.postXML(payUrl, xml, Consts.UTF_8);


            Map<String, String> resultMap = WXPayUtil.xmlToMap(postXML);
            LOGGER.info("调用微信预支付接口返回数据--->{}",resultMap);
            //通信不成功
            if(!"SUCCESS".equals(resultMap.get("return_code"))){
                LOGGER.error("交易通信失败返回错误描述--->{}",resultMap.get("return_msg"));
                return Msg.error("生成订单失败");
            }

            //交易不成功
            if(!"SUCCESS".equals(resultMap.get("result_code"))){
                LOGGER.error("交易失败返回错误描述--->{}",resultMap.get("err_code_des"));
                return Msg.error("生成订单失败");
            }

            Map<String,String> map = new HashMap<>();
            map.put("appId",appId);
            map.put("timeStamp",System.currentTimeMillis()+"");
            map.put("nonceStr",WXPayUtil.generateNonceStr());
            map.put("package","prepay_id="+resultMap.get("prepay_id"));
            map.put("signType", "MD5");

            map.put("paySign",WXPayUtil.generateSignature(map,signKey, WXPayConstants.SignType.MD5));

            //生成交易记录
            PayInfo payInfo = new PayInfo();
            payInfo.setPay(0);
            payInfo.setCreateTime(new Date());
            User user = usrDao.findByOpenId(openid);
            payInfo.setMobile(user!=null?user.getMobile():null);
            payInfo.setOpenId(openid);
            payInfo.setOrderId(paramsMap.get("out_trade_no"));
            payInfo.setPrepayId(resultMap.get("prepay_id"));
            payInfoDao.save(payInfo);
            return Msg.success("创建下单成功",map);

        } catch (Exception e) {
            LOGGER.error("签名异常--->{}",e);
            return Msg.error("生成订单失败");
        }

    }


    @Override
    public void callBack(Map<String, String> notifyMap) {

        if(notifyMap.get("return_code").equals("SUCCESS")) {
            if (notifyMap.get("result_code").equals("SUCCESS")) {
                String tradeNo = notifyMap.get("out_trade_no");//商户订单号
                String amountpaid = notifyMap.get("total_fee");//实际支付的订单金额:单位 分

                //跟新订单数据
                PayInfo payInfo = payInfoDao.findByOrderId(tradeNo);
                if(payInfo!=null && payInfo.getPay()!=1){
                    payInfo.setPay(1);
                    payInfoDao.saveAndFlush(payInfo);

                    //激活用户
                    User user = usrDao.findByMobile(payInfo.getMobile());
                    if(user!=null){
                        user.setActivated(1);
                        usrDao.saveAndFlush(user);
                    }


                    //生成充值记录表
                    Charge charge = new Charge();
                    charge.setAmt(Double.valueOf(amountpaid));
                    charge.setChargeTime(new Date());
                    charge.setUserId(user.getId());
                    chargeDao.save(charge);

                    //被邀请 加邀请人奖励金额
                    if(user.getRecommender()!=null){

                        SysCfg sysCfg = sysCfgDao.findByCodeAndYn(SysCodeEnum.PAY_INVITE_NUM.getValue(), 1);
                        User recommender = usrDao.findById(user.getRecommender()).get();
                        if(recommender!=null && sysCfg!=null){

                            Double rewardAmt = Double.parseDouble(sysCfg.getCodeVal());
                            if(recommender.getRewardAmt()==null){
                                recommender.setRewardAmt(rewardAmt);
                            }else {
                                BigDecimal result = new BigDecimal(Double.toString(recommender.getRewardAmt())).add(new BigDecimal(Double.toString(rewardAmt)));
                                recommender.setRewardAmt(result.doubleValue());
                            }
                            usrDao.saveAndFlush(recommender);


                            //
                            Reward reward = new Reward();
                            reward.setAmt(Double.valueOf(sysCfg.getCodeVal()));
                            reward.setChargeId(charge.getId());
                            reward.setPaid(0);
                            reward.setPaidTime(null);
                            reward.setPayer(null);
                            reward.setUserId(recommender.getId());
                            reward.setRewardTime(new Date());
                            rewardDao.save(reward);

                        }
                    }

                }
            }
        }
    }
}
