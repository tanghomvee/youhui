package com.homvee.youhui.common.vos;

import java.io.Serializable;

/**
 * token缓存
 */
public class TokenBean implements Serializable {

  //{"access_token":"ACCESS_TOKEN","expires_in":7200}
    private String accessToken;

    private Long expires;

    //获取时间
    private Long createTime;

    public boolean isExpires(){
        return System.currentTimeMillis()>createTime+expires*1000;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
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
