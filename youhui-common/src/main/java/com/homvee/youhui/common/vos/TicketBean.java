package com.homvee.youhui.common.vos;

import java.io.Serializable;

public class TicketBean implements Serializable {

    //{
    //"errcode":0,
    //"errmsg":"ok",
    //"ticket":"bxLdikRXVbTPdHSM05e5u5sUoXNKd8-41ZO3MhKoyN5OfkWITDGgnr2fwJ0m9E8NYzWKVZvdVtaUgWvsdshFKA",
    //"expires_in":7200
    //}

    private Integer errcode;

    private String errmsg;

    private String ticket;

    private Long expires;

    private Long createTime;

    public boolean isExpires(){
        return System.currentTimeMillis()>createTime+expires;
    }

    public Integer getErrcode() {
        return errcode;
    }

    public void setErrcode(Integer errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public Long getExpires() {
        return expires;
    }

    public void setExpires(Long expires) {
        this.expires = expires;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }
}
