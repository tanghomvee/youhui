package com.homvee.youhui.service.wechat;


import com.homvee.youhui.common.vos.Msg;

import java.util.Map;

public interface WeChatService {
    Msg getUsrOpenId(String code, String state);


    String doCallback(String signature, String timestamp, String nonce, String echostr);

    Msg jsapi(String url);

    Map<String,Object> getUserInfo(String openid);
}
