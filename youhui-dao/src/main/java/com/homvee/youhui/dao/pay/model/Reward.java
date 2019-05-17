package com.homvee.youhui.dao.pay.model;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="reward")
public class Reward implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name="chargeId")
    private Long chargeId;

    @Column(name="userId")
    private Long userId;

    @Column(name="amt")
    private Double amt;

    @Column(name="paid")
    private Integer paid;

    @Column(name="paidTime")
    private Date paidTime;

    @Column(name="rewardTime")
    private Date rewardTime;

    @Column(name="payer")
    private String payer;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getChargeId() {
        return chargeId;
    }

    public void setChargeId(Long chargeId) {
        this.chargeId = chargeId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Double getAmt() {
        return amt;
    }

    public void setAmt(Double amt) {
        this.amt = amt;
    }

    public Integer getPaid() {
        return paid;
    }

    public void setPaid(Integer paid) {
        this.paid = paid;
    }

    public Date getPaidTime() {
        return paidTime;
    }

    public void setPaidTime(Date paidTime) {
        this.paidTime = paidTime;
    }

    public Date getRewardTime() {
        return rewardTime;
    }

    public void setRewardTime(Date rewardTime) {
        this.rewardTime = rewardTime;
    }

    public String getPayer() {
        return payer;
    }

    public void setPayer(String payer) {
        this.payer = payer;
    }

    @Override
    public String toString() {
        return "RewardDao{" +
                "id=" + id +
                ", chargeId=" + chargeId +
                ", userId=" + userId +
                ", amt=" + amt +
                ", paid=" + paid +
                ", paidTime=" + paidTime +
                ", rewardTime=" + rewardTime +
                ", payer='" + payer + '\'' +
                '}';
    }
}
