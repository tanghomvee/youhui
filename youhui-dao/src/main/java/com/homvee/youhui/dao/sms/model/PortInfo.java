package com.homvee.youhui.dao.sms.model;

import com.homvee.youhui.dao.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "port_info")
public class PortInfo extends BaseEntity {

    /**
     * 端口号
     */
    private Integer portNum;
    /**
     * 用户识别码(IMSI)
     */
    private String imsi;
    /**
     * 卡识别码(ICCID)
     */
    private String iccid;

    /**
     * 手机号
     */
    private  String phoNum;

    public Integer getPortNum() {
        return portNum;
    }

    public void setPortNum(Integer portNum) {
        this.portNum = portNum;
    }

    public String getImsi() {
        return imsi;
    }

    public void setImsi(String imsi) {
        this.imsi = imsi;
    }

    public String getIccid() {
        return iccid;
    }

    public void setIccid(String iccid) {
        this.iccid = iccid;
    }

    public String getPhoNum() {
        return phoNum;
    }

    public void setPhoNum(String phoNum) {
        this.phoNum = phoNum;
    }
}
