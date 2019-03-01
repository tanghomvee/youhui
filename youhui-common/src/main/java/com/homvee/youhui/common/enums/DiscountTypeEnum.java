package com.homvee.youhui.common.enums;

public enum DiscountTypeEnum {

    NORMAL(1,"满减") , AUTO(2,"打折"),STOP(3,"免费"),LOOP(4,"不知道");

    private Integer val;
    private String desc;

    public Integer getVal() {
        return val;
    }

    public String getDesc() {
        return desc;
    }

    DiscountTypeEnum(Integer val, String desc) {
        this.val = val;
        this.desc = desc;
    }


    public  static DiscountTypeEnum getByVal(Integer val){
        for (DiscountTypeEnum tmp : DiscountTypeEnum.values()){
            if(tmp.val.equals(val)){
                return tmp;
            }
        }
        return null;
    }
}
