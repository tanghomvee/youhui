package com.homvee.youhui.dao.user.model;

import com.homvee.youhui.dao.BaseEntity;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 
 */
@Entity
@Table(name="user")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name="mobile")
    private String mobile;

    @Column(name="pwd")
    private String pwd;

    @Column(name="rewardAmt")
    private Double rewardAmt;

    @Column(name="recommender")
    private Long recommender;

    @Column(name="activated")
    private Integer activated;

    @Column(name="openId")
    private String openId;

    @Column(name="creator")
    private String creator;

    @Column(name="createTime")
    private Date createTime;

    @Column(name="changer")
    private String changer;

    @Column(name="changeTime")
    private Date changeTime;

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

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public Double getRewardAmt() {
        return rewardAmt;
    }

    public void setRewardAmt(Double rewardAmt) {
        this.rewardAmt = rewardAmt;
    }

    public Long getRecommender() {
        return recommender;
    }

    public void setRecommender(Long recommender) {
        this.recommender = recommender;
    }

    public Integer getActivated() {
        return activated;
    }

    public void setActivated(Integer activated) {
        this.activated = activated;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getChanger() {
        return changer;
    }

    public void setChanger(String changer) {
        this.changer = changer;
    }

    public Date getChangeTime() {
        return changeTime;
    }

    public void setChangeTime(Date changeTime) {
        this.changeTime = changeTime;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", mobile='" + mobile + '\'' +
                ", pwd='" + pwd + '\'' +
                ", rewardAmt=" + rewardAmt +
                ", recommender=" + recommender +
                ", activated=" + activated +
                ", openId='" + openId + '\'' +
                ", creator='" + creator + '\'' +
                ", createTime=" + createTime +
                ", changer='" + changer + '\'' +
                ", changeTime=" + changeTime +
                '}';
    }
}