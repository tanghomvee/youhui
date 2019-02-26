package com.homvee.youhui.common.vos;

import java.util.Date;

/**
 * Copyright (c) 2018$. ddyunf.com all rights reserved
 *
 * @author Homvee.Tang(tanghongwei @ ddcloudf.com)
 * @version V1.0
 * @Description TODO(用一句话描述该文件做什么)
 * @date 2018-08-17 11:18
 */
public class ContentVO extends BaseVO {

    private Long roomId;

    private String roomName;

    private Long acctId;
    private String acctName;

    private Long userId;
    private String userName;
    /**
     * 使用次数
     */
    private Integer used;

    /**
     * 内容分类
     */
    private Long catgId;
    private Long parentCatgId;
    private String catgName;

    /**
     * 最近使用时间
     */
    private Date recentUsedTime;


    private String content;

    /**
     * 上一条聊天的内容ID
     */
    private Long preId;
    private String preContent;

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public Long getAcctId() {
        return acctId;
    }

    public void setAcctId(Long acctId) {
        this.acctId = acctId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getUsed() {
        return used;
    }

    public void setUsed(Integer used) {
        this.used = used;
    }

    public Long getCatgId() {
        return catgId;
    }

    public void setCatgId(Long catgId) {
        this.catgId = catgId;
    }

    public Date getRecentUsedTime() {
        return recentUsedTime;
    }

    public void setRecentUsedTime(Date recentUsedTime) {
        this.recentUsedTime = recentUsedTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getPreId() {
        return preId;
    }

    public void setPreId(Long preId) {
        this.preId = preId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getAcctName() {
        return acctName;
    }

    public void setAcctName(String acctName) {
        this.acctName = acctName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCatgName() {
        return catgName;
    }

    public void setCatgName(String catgName) {
        this.catgName = catgName;
    }

    public String getPreContent() {
        return preContent;
    }

    public void setPreContent(String preContent) {
        this.preContent = preContent;
    }

    public Long getParentCatgId() {
        return parentCatgId;
    }

    public void setParentCatgId(Long parentCatgId) {
        this.parentCatgId = parentCatgId;
    }
}
