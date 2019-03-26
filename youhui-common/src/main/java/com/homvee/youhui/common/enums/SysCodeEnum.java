package com.homvee.youhui.common.enums;

public enum SysCodeEnum {


    /**
     * 充值金额
     */
    PAY_MONEY("PAY_MONEY");

    private String value;

    private SysCodeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
