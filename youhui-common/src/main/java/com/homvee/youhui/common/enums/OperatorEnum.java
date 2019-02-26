package com.homvee.youhui.common.enums;

/**
 * 操作符
 *
 * @author ddyunf
 */
public enum OperatorEnum {
	SMS_CHECK("SMS_CHECK","短信校验"),
	ROOM_CHECK("ROOM_CHECK","房间校验"),
	CHAT("CHAT","房间发言"),
	HEART_CHECK("HEART_CHECK","心跳检测"),
	;

	private String val;
	private String desc;

	public String getVal() {
		return val;
	}

	public String getDesc() {
		return desc;
	}

	OperatorEnum(String val, String desc) {
		this.val = val;
		this.desc = desc;
	}


	public  static OperatorEnum getByVal(String val){
		for (OperatorEnum tmp : OperatorEnum.values()){
			if(tmp.val.equals(val)){
				return tmp;
			}
		}
		return null;
	}

}
