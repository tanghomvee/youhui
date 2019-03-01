package com.homvee.youhui.service.wechat;


import com.homvee.youhui.common.vos.Msg;

public interface WeChatService {
    Msg getUsrOpenId(String code, String state);

    void pushTemplateMsg2WeChat(Object msg);

    String doCallback(String signature, String timestamp, String nonce, String echostr);

}
