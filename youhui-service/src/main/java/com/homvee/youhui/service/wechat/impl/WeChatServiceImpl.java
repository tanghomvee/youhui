package com.homvee.youhui.service.wechat.impl;

import com.alibaba.fastjson.JSONObject;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.homvee.youhui.common.utils.HttpUtils;
import com.homvee.youhui.common.utils.WXBizMsgCrypt;
import com.homvee.youhui.common.vos.Msg;
import com.homvee.youhui.service.wechat.WeChatService;
import org.apache.http.Consts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import sun.misc.MessageUtils;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

import static com.homvee.youhui.common.constants.WeChatKey.*;


@Service("weChatService")
public class WeChatServiceImpl implements WeChatService {
    private static Logger LOGGER = LoggerFactory.getLogger(WeChatServiceImpl.class);

    private static String  APP_ID;
    private static String  APP_SECRET;
    private static String  ORG_GRANT_TYPE;
    private static String  USR_GRANT_TYPE;

    private static Long EXPIRE_TIME = 7200L;

    private static String MSG_TMP_URL;
    private static String ACCESS_TOKEN_URL;
    private static String USR_OPENID_URL;


    @Resource
    private WXBizMsgCrypt wxBizMsgCrypt;

    private static LoadingCache<String , String> ORG_ACCESS_TOKEN_CACHE = CacheBuilder.newBuilder()
            .refreshAfterWrite(EXPIRE_TIME, TimeUnit.SECONDS)
            .expireAfterAccess(EXPIRE_TIME, TimeUnit.SECONDS)
            .maximumSize(5)// 设置缓存个数
            .build(
                    new CacheLoader<String, String>() {
                        /** 当本地缓存命没有中时，调用load方法获取结果并将结果缓存 **/
                        @Override
                        public String load(String s) throws Exception {
                            return refreshOrgAccessToken();
                        }
                    }
            );



    @Override
    public Msg getUsrOpenId(String code , String state){
//        String usrOpenIdUrl = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + APP_ID + "&secret=" + APP_SECRET + "&grant_type=" + USR_GRANT_TYPE + "&code="+code;
        String usrOpenIdUrl = USR_OPENID_URL + code;
        String body = null;
        JSONObject retJson = null;
        try{

            body = HttpUtils.postForm(usrOpenIdUrl);
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

    @Override
    public void pushTemplateMsg2WeChat(Object msg) {

        String token = null;
        JSONObject retJson = null;
        int retryNum = 3;
        for (int i = 0; i < retryNum ; i ++ ){
            try{
                token = ORG_ACCESS_TOKEN_CACHE.get(ORG_ACCESS_TOKEN_KEY);
                String body = HttpUtils.postJSON(MSG_TMP_URL + token , msg , Consts.UTF_8);
                retJson = JSONObject.parseObject(body);
            }catch (Exception ex){
                LOGGER.error("发送模板消息[{}]异常"  , msg ,ex);
            }
            if(!retJson.containsKey(ERR_KEY)){
                LOGGER.info("发送模板消息[{}]失败:{}" , msg , retJson);
                continue;
            }
            String retCode = retJson.getString(ERR_KEY);
            if("0".equals(retCode)){
                LOGGER.info("发送模板消息[{}]成功" , msg);
                return;
            }
            if("-1".equals(retCode)){
                try {
                    Thread.sleep(1000L);
                } catch (Exception e) {
                    LOGGER.error("" , e);
                }
            }
            //ACCESS_TOKEN超时
            if("42001".equals(retCode)){
                ORG_ACCESS_TOKEN_CACHE.refresh(ORG_ACCESS_TOKEN_KEY);
            }
        }
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


    private static String refreshOrgAccessToken() {
        //"https://api.weixin.qq.com/cgi-bin/token?grant_type=" + ORG_GRANT_TYPE +"&appid=" + APP_ID+ "&secret=" + APP_SECRET;
        String orgAccessTokenUrl =  ACCESS_TOKEN_URL;
        JSONObject retJson = null;
        String body = null;
        try{
            body = HttpUtils.postForm(orgAccessTokenUrl);
            LOGGER.info("获取微信公众号token{}" , body);
            retJson = JSONObject.parseObject(body);
        }catch (Exception ex){
            LOGGER.error("获取微信公众号token信息异常"  ,ex);
            return body;
        }
        if(retJson.containsKey(ERR_KEY)){

            LOGGER.info("获取微信公众号token失败:{}" , retJson);

        }else{

            body = retJson.getString(ORG_ACCESS_TOKEN_KEY);
            Long expires = retJson.getLong(ORG_ACCESS_TOKEN_EXPIRES_KEY);

            if(!EXPIRE_TIME.equals(expires)){
                EXPIRE_TIME = expires;
                ORG_ACCESS_TOKEN_CACHE.cleanUp();
                ORG_ACCESS_TOKEN_CACHE = CacheBuilder.newBuilder()
                        .refreshAfterWrite(expires, TimeUnit.SECONDS)
                        .expireAfterAccess(expires, TimeUnit.SECONDS)
                        .maximumSize(5)// 设置缓存个数
                        .build(
                                new CacheLoader<String, String>() {
                                    /** 当本地缓存命没有中时，调用load方法获取结果并将结果缓存 **/
                                    @Override
                                    public String load(String s) throws Exception {
                                        return refreshOrgAccessToken();
                                    }
                                }
                        );
            }
        }
        return body;
    }

    @Value("${wechat.app.id}")
    public  void setAppId(String appId) {
        APP_ID = appId;
    }

    @Value("${wechat.app.secret}")
    public  void setAppSecret(String appSecret) {
        APP_SECRET = appSecret;
    }

    @Value("${wechat.org.grant.type}")
    public  void setOrgGrantType(String orgGrantType) {
        ORG_GRANT_TYPE = orgGrantType;
    }

    @Value("${wechat.usr.openid.url}")
    public  void setUsrGrantType(String usrGrantType) {
        USR_GRANT_TYPE = usrGrantType;
    }

    @Value("${wechat.org.access.token.expire}")
    public  void setExpireTime(Long expireTime) {
        EXPIRE_TIME = expireTime;
    }

    @Value("${wechat.msg.tmp.url}")
    public  void setMsgTmpUrl(String msgTmpUrl) {
        MSG_TMP_URL = msgTmpUrl;
    }

    @Value("${wechat.org.access.token.url}")
    public  void setAccessTokenUrl(String accessTokenUrl) {
        ACCESS_TOKEN_URL = accessTokenUrl;
    }

    @Value("${wechat.usr.openid.url}")
    public  void setUsrOpenidUrl(String usrOpenidUrl) {
        USR_OPENID_URL = usrOpenidUrl;
    }


}
