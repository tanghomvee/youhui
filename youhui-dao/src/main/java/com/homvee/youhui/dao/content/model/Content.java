package com.homvee.youhui.dao.content.model;

import com.homvee.youhui.dao.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * Copyright (c) 2018$. ddyunf.com all rights reserved
 *
 * @author Homvee.Tang(tanghongwei @ ddcloudf.com)
 * @version V1.0
 * @Description TODO(用一句话描述该文件做什么)
 * @date 2018-08-16 11:01
 */
@Entity
@Table(name = "t_content")
public class Content extends BaseEntity {

    private Long roomId;

    private Long acctId;

    private Long userId;
    /**
     * 1,0
     * 是否已使用
     */
    private Integer used;

    /**
     * 内容分类
     */
    private Long catgId;

    /**
     * 最近使用时间
     */
    private Date recentUsedTime;


    private String content;

    /**
     * 上一条聊天的内容ID
     */
    private Long preId;

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
}
