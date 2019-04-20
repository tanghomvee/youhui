package com.homvee.youhui.common.enums;

public enum SysCodeEnum {


    /**
     * 充值金额
     */
    PAY_MONEY("PAY_MONEY"),

    SHARE_INFO_HEAD("SHARE_INFO_HEAD"),

    SHARE_INFO_TITLE("SHARE_INFO_TITLE"),

    SHARE_INFO_LISTINFO("SHARE_INFO_LISTINFO"),

    ;

    private String value;

    private SysCodeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
