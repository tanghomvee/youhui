package com.homvee.youhui.service.wechat.impl;

import com.alibaba.fastjson.JSONObject;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.homvee.youhui.common.enums.SysCodeEnum;
import com.homvee.youhui.common.utils.HttpUtils;
import com.homvee.youhui.common.utils.WXBizMsgCrypt;
import com.homvee.youhui.common.vos.*;
import com.homvee.youhui.dao.cfg.SysCfgDao;
import com.homvee.youhui.dao.cfg.model.SysCfg;
import com.homvee.youhui.service.discount.DiscountService;
import com.homvee.youhui.service.discount.impl.DiscountServiceImpl;
import com.homvee.youhui.service.wechat.WeChatService;
import org.apache.http.Consts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import sun.misc.MessageUtils;

import javax.annotation.Resource;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.homvee.youhui.common.constants.WeChatKey.*;


@Service("weChatService")
public class WeChatServiceImpl implements WeChatService {
    private static Logger LOGGER = LoggerFactory.getLogger(WeChatServiceImpl.class);

    @Value("${wechat.app.id}")
    private String appId;

    @Value("${wechat.app.secret}")
    private String appSecret;

    @Value("${wechat.crypt.token}")
    private String cryptToken;

    @Resource
    private WXBizMsgCrypt wxBizMsgCrypt;

    @Autowired
    private SysCfgDao sysCfgDao;

    @Autowired
    private DiscountService discountService;


    private TokenBean tokenBean;


    private TicketBean ticketBean;

    @Override
    public Map<String, Object> getUserInfo(String openid) {

        Map<String,Object> resultMap = new HashMap<>();
        String token = getToken();
        String url = "https://api.weixin.qq.com/cgi-bin/user/info?access_token="+token+"&openid="+openid+"&lang=zh_CN";
        try {
            String body = HttpUtils.postForm(url);
            LOGGER.info("获取用户信息{}" , body);
            JSONObject retJson = JSONObject.parseObject(body);
            if(retJson.containsKey(ERR_KEY)){
                LOGGER.info("获取用户信息异常失败:{}" , retJson);
                return resultMap;
            }
            resultMap.put("nickname",retJson.getString("nickname"));
            resultMap.put("sex",retJson.getString("sex"));
            resultMap.put("language",retJson.getString("language"));
            resultMap.put("city",retJson.getString("city"));
            resultMap.put("province",retJson.getString("province"));
            resultMap.put("country",retJson.getString("country"));
            resultMap.put("headimgurl",retJson.getString("headimgurl"));

        }catch (Exception e){
            LOGGER.error("获取用户信息异常" ,e);
        }
        return resultMap;
    }

    @Override
    public Msg getUsrOpenId(String code , String state){
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="+appId+"&secret="+appSecret+"&code="+code+"&grant_type=authorization_code";
        String body = null;
        JSONObject retJson = null;
        try{
            body = HttpUtils.postForm(url);
            LOGGER.info("通过用户code={}获取用户信息结果:{}" , code , body);
            retJson = JSONObject.parseObject(body);
        }catch (Exception ex){
            LOGGER.error("通过用户code={}获取用户信息异常" ,code ,ex);
            return Msg.error("获取openId失败");
        }
        if(retJson.containsKey(ERR_KEY)){
            LOGGER.info("通过用户code={}获取用户的OPENID失败:{}" , code , retJson);
            return Msg.error(retJson.getString(ERR_MSG_KEY));
        }
        String openId = retJson.getString(OPENID_KEY);
        return Msg.success((Object) openId);
    }


    private String getToken(){
        //没有或者过期 重新获取
        if(tokenBean==null || tokenBean.isExpires()){
            String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="+appId+"&secret="+appSecret;
            try {
                String body = HttpUtils.postForm(url);
                LOGGER.info("获取token信息{}" , body);
                JSONObject retJson = JSONObject.parseObject(body);
                if(retJson.containsKey(ERR_KEY)){
                    LOGGER.info("获取token信息异常失败:{}" , retJson);
                    return null;
                }
                TokenBean tokenBean = new TokenBean();
                tokenBean.setAccessToken(retJson.getString("access_token"));
                tokenBean.setExpires(retJson.getLong("expires_in"));
                tokenBean.setCreateTime(System.currentTimeMillis());
                this.tokenBean = tokenBean;
            }catch (Exception e){
                LOGGER.error("获取token信息异常" ,e);
            }
        }
        return tokenBean.getAccessToken();
    }

    private String getTicket(){
        //没有或者过期 重新获取
        if(ticketBean==null || ticketBean.isExpires()){
            String access_token = getToken();
            String url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token="+access_token+"&type=jsapi";
            try {
                String body = HttpUtils.postForm(url);
                LOGGER.info("获取ticket信息{}" , body);
                //{
                //"errcode":0,
                //"errmsg":"ok",
                //"ticket":"bxLdikRXVbTPdHSM05e5u5sUoXNKd8-41ZO3MhKoyN5OfkWITDGgnr2fwJ0m9E8NYzWKVZvdVtaUgWvsdshFKA",
                //"expires_in":7200
                //}
                JSONObject retJson = JSONObject.parseObject(body);

                TicketBean ticketBean = new TicketBean();
                String errmsg = retJson.getString("errmsg");
                if(!"ok".equals(errmsg)){
                    LOGGER.info("获取ticket信息异常失败:{}" , retJson);
                    return null;
                }
                ticketBean.setErrcode(retJson.getInteger("errcode"));
                ticketBean.setCreateTime(System.currentTimeMillis());
                ticketBean.setErrmsg(retJson.getString("errmsg"));
                ticketBean.setTicket(retJson.getString("ticket"));
                ticketBean.setExpires(retJson.getLong("expires_in"));
                this.ticketBean = ticketBean;
            }catch (Exception e){
                LOGGER.error("获取ticket信息异常" ,e);
            }
        }
        return ticketBean.getTicket();
    }

    @Override
    public Msg jsapi(String url) {

        Map<String, String> map = sign(getTicket(), url);
        map.put("appid",appId);
        //map.put("accessToken",accessToken);
        //Map<String, String> map = sign(ticket, "http://weixin.ddyunf.com");
        LOGGER.info("获取jsapi签名api{}",map);
        return Msg.success("获取ticket成功",map);
    }

    public static Map<String, String> sign(String jsapi_ticket, String url) {
        Map<String, String> ret = new HashMap<String, String>();
        String nonce_str = create_nonce_str();
        String timestamp = create_timestamp();
        String string1;
        String signature = "";

        //注意这里参数名必须全部小写，且必须有序
        string1 = "jsapi_ticket=" + jsapi_ticket +
                "&noncestr=" + nonce_str +
                "&timestamp=" + timestamp +
                "&url=" + url;
        System.out.println(string1);

        try{
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            signature =SHA1(string1);
        }catch (Exception e){
            e.printStackTrace();
        }
        ret.put("url", url);
        ret.put("jsapi_ticket", jsapi_ticket);
        ret.put("nonceStr", nonce_str);
        ret.put("timestamp", timestamp);
        ret.put("signature", signature);
        return ret;
    }

    private static String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash){
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }
    public static String SHA1(String decript) {
        try {
            MessageDigest digest = java.security.MessageDigest.getInstance("SHA-1");
            digest.update(decript.getBytes());
            byte messageDigest[] = digest.digest();
            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            // 字节数组转换为 十六进制 数
            for (int i = 0; i < messageDigest.length; i++) {
                String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
                if (shaHex.length() < 2) {
                    hexString.append(0);
                }
                hexString.append(shaHex);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    //生成随机字符串
    private static String create_nonce_str() {
        return UUID.randomUUID().toString().replaceAll("-" , "");
    }
    //生成时间戳字符串
    private static String create_timestamp() {
        return Long.toString(System.currentTimeMillis() / 1000);
    }



    @Override
    public String doCallback(String signature, String timestamp, String nonce, String echostr) {
        String ret = "";
        try {

            if( wxBizMsgCrypt.verifyUrl(signature , timestamp ,nonce ,echostr) ){
                return echostr;
            }
        }catch (Exception ex){
            LOGGER.error("微信回掉校验失败" , ex);
        }

        return ret;

    }


    @Override
    public Msg shareInfo() {
        Map<String,Object> resultMap = new HashMap<>();
        SysCfg sysCfgHead = sysCfgDao.findByCodeAndYn(SysCodeEnum.SHARE_INFO_HEAD.getValue(), 1);
        SysCfg sysCfgTitle = sysCfgDao.findByCodeAndYn(SysCodeEnum.SHARE_INFO_TITLE.getValue(), 1);
        SysCfg sysCfgListInfo = sysCfgDao.findByCodeAndYn(SysCodeEnum.SHARE_INFO_LISTINFO.getValue(), 1);
        resultMap.put("head",sysCfgHead!=null?sysCfgHead.getCodeVal():"");
        resultMap.put("title",sysCfgTitle!=null?sysCfgTitle.getCodeVal():"");
        resultMap.put("listInfo",sysCfgListInfo!=null?sysCfgListInfo.getCodeVal():"");

        List<Map<String,Object>> listData = new ArrayList<>();
        //随机三个列表
        DiscountVo discountVo = new DiscountVo();
        Msg msg = discountService.findByCondition(discountVo);
        Pager pager = (Pager)msg.getData();
        if(pager.getData()!=null && pager.getData().size()>0){
            for (int i=0;i<pager.getData().size();i++){
                Map<String,Object> temp = (Map<String,Object>)(pager.getData().get(i));
                listData.add(temp);
                if(i>2){
                    break;
                }
            }
        }
        resultMap.put("listData",listData);
        return Msg.success("获取分享数据成功",resultMap);
    }
}
