package com.homvee.youhui.dao.sms.model;

import com.homvee.youhui.dao.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "sms_send")
public class SendingSMS extends BaseEntity {

    /**
     * 大于0表示指定端口号发送
     */
    private Integer portNum;
    /**
     * 接收号码
     */
    private String smsNumber;
    /**
     * 彩信标题，如果发送彩信不能为空
     */
    private String smsSubject;
    /**
     * 发送内容
     */
    private String smsContent;

    /**
     * 0:短信 1:彩信
     */
    private Integer smsType;
    /**
     * 手机号
     */
    private  String phoNum;
    /**
     * 0:未发送 1:已在发送队列 2:发送成功 3:发送失败
     */
    private Integer smsState;

    public Integer getPortNum() {
        return portNum;
    }

    public void setPortNum(Integer portNum) {
        this.portNum = portNum;
    }

    public String getSmsNumber() {
        return smsNumber;
    }

    public void setSmsNumber(String smsNumber) {
        this.smsNumber = smsNumber;
    }

    public String getSmsSubject() {
        return smsSubject;
    }

    public void setSmsSubject(String smsSubject) {
        this.smsSubject = smsSubject;
    }

    public String getSmsContent() {
        return smsContent;
    }

    public void setSmsContent(String smsContent) {
        this.smsContent = smsContent;
    }

    public Integer getSmsType() {
        return smsType;
    }

    public void setSmsType(Integer smsType) {
        this.smsType = smsType;
    }

    public String getPhoNum() {
        return phoNum;
    }

    public void setPhoNum(String phoNum) {
        this.phoNum = phoNum;
    }

    public Integer getSmsState() {
        return smsState;
    }

    public void setSmsState(Integer smsState) {
        this.smsState = smsState;
    }

    @Override
    public String toString() {
        return "SendingSMS{" +
                "portNum=" + portNum +
                ", smsNumber='" + smsNumber + '\'' +
                ", smsSubject='" + smsSubject + '\'' +
                ", smsContent='" + smsContent + '\'' +
                ", smsType=" + smsType +
                ", phoNum='" + phoNum + '\'' +
                ", smsState=" + smsState +
                '}';
    }
}
