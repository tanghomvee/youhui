package com.homvee.youhui.common.enums;

public enum SysCodeEnum {


    /**
     * 充值金额
     */
    PAY_MONEY("PAY_MONEY"),

    SHARE_INFO_HEAD("SHARE_INFO_HEAD"),

    SHARE_INFO_TITLE("SHARE_INFO_TITLE"),

    SHARE_INFO_LISTINFO("SHARE_INFO_LISTINFO"),

    PAY_INVITE_NUM("PAY_INVITE_NUM"),

    FENH_LOGO("FENH_LOGO"),

    FENH_TITLE("FENH_TITLE"),
    ;

    private String value;

    private SysCodeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
