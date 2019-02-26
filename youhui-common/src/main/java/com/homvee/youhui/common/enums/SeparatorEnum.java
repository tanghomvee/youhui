package com.homvee.youhui.common.enums;

/**
 * 分割符号
 *
 * @author ddyunf
 */
public enum SeparatorEnum {
	UNDERLINE("_","下划线"),
	COMMA(",","逗号"),
	MIDDLE_LINE("-","中划线"),
	;

	private String val;
	private String desc;

	public String getVal() {
		return val;
	}

	public String getDesc() {
		return desc;
	}

	SeparatorEnum(String val, String desc) {
		this.val = val;
		this.desc = desc;
	}


	public  static SeparatorEnum getByVal(String val){
		for (SeparatorEnum tmp : SeparatorEnum.values()){
			if(tmp.val.equals(val)){
				return tmp;
			}
		}
		return null;
	}

}
