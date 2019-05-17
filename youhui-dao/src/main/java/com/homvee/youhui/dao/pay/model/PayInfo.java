package com.homvee.youhui.dao.pay.model;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="payinfo")
public class PayInfo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name="mobile")
    private String mobile;

    @Column(name="orderId")
    private String orderId;

    @Column(name="openId")
    private String openId;

    @Column(name="prepayId")
    private String prepayId;

    @Column(name="pay")
    private Integer pay;

    @Column(name="createTime")
    private Date createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getPrepayId() {
        return prepayId;
    }

    public void setPrepayId(String prepayId) {
        this.prepayId = prepayId;
    }

    public Integer getPay() {
        return pay;
    }

    public void setPay(Integer pay) {
        this.pay = pay;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "PayInfo{" +
                "id=" + id +
                ", mobile='" + mobile + '\'' +
                ", orderId='" + orderId + '\'' +
                ", openId='" + openId + '\'' +
                ", prepayId='" + prepayId + '\'' +
                ", pay=" + pay +
                ", createTime=" + createTime +
                '}';
    }
}
