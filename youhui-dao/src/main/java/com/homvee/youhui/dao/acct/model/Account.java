package com.homvee.youhui.dao.acct.model;

import com.homvee.youhui.dao.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Copyright (c) 2018$. ddyunf.com all rights reserved
 *
 * @author Homvee.Tang(tanghongwei @ ddcloudf.com)
 * @version V1.0
 * @Description TODO(用一句话描述该文件做什么)
 * @date 2018-08-16 11:01
 */
@Entity
@Table(name = "t_account")
public class Account extends BaseEntity {

    private String acctName;

    private String mobile;

    private Long userId;

    private Integer period;

    public String getAcctName() {
        return acctName;
    }

    public void setAcctName(String acctName) {
        this.acctName = acctName;
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

    public Integer getPeriod() {
        return period;
    }

    public void setPeriod(Integer period) {
        this.period = period;
    }

    @Override
    public String toString() {
        return "Account{" +
                "acctName='" + acctName + '\'' +
                ", mobile='" + mobile + '\'' +
                ", userId=" + userId +
                ", period=" + period +
                '}';
    }
}
