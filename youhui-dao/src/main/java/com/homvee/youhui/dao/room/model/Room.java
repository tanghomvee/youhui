package com.homvee.youhui.dao.room.model;

import com.homvee.youhui.dao.BaseEntity;
import com.homvee.youhui.common.enums.WayEnum;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Copyright (c) 2018$. ddyunf.com all rights reserved
 *
 * @author Homvee.Tang(tanghongwei @ ddcloudf.com)
 * @version V1.0
 * @Description TODO(用一句话描述该文件做什么)
 * @date 2018-08-16 09:47
 */
@Entity
@Table(name = "t_room")
public class Room extends BaseEntity {

    private String roomName;

    private String url;

    private Long userId;

    /**
     * @see WayEnum
     * 房间聊天方式
     */
    private Integer way;

    /**
     * 主播手机号
     */
    private String mobile;
    /**
     * 开始时间
     */
    private Integer startHour;

    /**
     * 结束时刻
     */
    private Integer endHour;

    /**
     * 每句对话内容的间隔时间单位秒
     */
    private Long intervalTime;

    /**
     * 房间默认内容
     */
    private String defaultContent;


    public Long getIntervalTime() {
        return intervalTime;
    }

    public void setIntervalTime(Long intervalTime) {
        this.intervalTime = intervalTime;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getWay() {
        return way;
    }

    public void setWay(Integer way) {
        this.way = way;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Integer getStartHour() {
        return startHour;
    }

    public void setStartHour(Integer startHour) {
        this.startHour = startHour;
    }

    public Integer getEndHour() {
        return endHour;
    }

    public void setEndHour(Integer endHour) {
        this.endHour = endHour;
    }


    public String getDefaultContent() {
        return defaultContent;
    }

    public void setDefaultContent(String defaultContent) {
        this.defaultContent = defaultContent;
    }

    @Override
    public String toString() {
        return "Room{" +
                "roomName='" + roomName + '\'' +
                ", url='" + url + '\'' +
                ", userId=" + userId +
                ", way=" + way +
                ", mobile='" + mobile + '\'' +
                ", startHour=" + startHour +
                ", endHour=" + endHour +
                ", intervalTime=" + intervalTime +
                ", defaultContent='" + defaultContent + '\'' +
                '}';
    }
}
