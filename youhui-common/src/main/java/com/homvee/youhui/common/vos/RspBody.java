package com.homvee.youhui.common.vos;

import com.homvee.youhui.common.enums.OperatorEnum;

import java.io.Serializable;

/**
 * Copyright (c) 2018$. ddyunf.com all rights reserved
 *
 * @author Homvee.Tang(tanghongwei @ ddcloudf.com)
 * @version V1.0
 * @Description TODO(用一句话描述该文件做什么)
 * @date 2018-09-29 15:31
 */
public class RspBody implements Serializable {

    private String operate;
    private Object content;

    public static  RspBody initChatBody(String content){
        return  new RspBody(OperatorEnum.CHAT.getVal() , content);
    }

    public static   RspBody initSMSCheckBody(Boolean content){
        return  new RspBody(OperatorEnum.SMS_CHECK.getVal() , content);
    }

    public static  RspBody initRoomCheckBody(String content){
        return  new RspBody(OperatorEnum.ROOM_CHECK.getVal() , content);
    }
    public static  RspBody initHeartbeatCheckBody(){
        return  new RspBody(OperatorEnum.HEART_CHECK.getVal() , null);
    }

    private RspBody(String operate, Object content) {
        this.operate = operate;
        this.content = content;
    }

    public String getOperate() {
        return operate;
    }

    public void setOperate(String operate) {
        this.operate = operate;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "RspBody{" +
                "operate='" + operate + '\'' +
                ", content=" + content +
                '}';
    }
}
