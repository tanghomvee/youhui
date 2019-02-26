package com.homvee.youhui.common.enums;

/**
 * 房间聊天方式
 *
 * @author ddyunf
 */
public enum WayEnum {
	NORMAL(1,"正常") , AUTO(2,"自动"),STOP(3,"停止"),LOOP(4,"循环");

	private Integer val;
	private String desc;

	public Integer getVal() {
		return val;
	}

	public String getDesc() {
		return desc;
	}

	WayEnum(Integer val, String desc) {
		this.val = val;
		this.desc = desc;
	}


	public  static WayEnum getByVal(Integer val){
		for (WayEnum tmp : WayEnum.values()){
			if(tmp.val.equals(val)){
				return tmp;
			}
		}
		return null;
	}

}
