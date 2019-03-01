package com.homvee.youhui.common.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by sun on 2017/7/19.
 */
public  class VerifyUtil {

    /**
     * 校验手机号
     * @param mobiles
     * @return
     */
    public static boolean isMobileNO(String mobiles){
        //String rgx = "^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$";
        String rgx="^((13[0-9])|(14[0-9])|(15[0-9])|(16[0-9])|(18[0-9])|(17[0-9])|(19[0-9]))\\d{8}$";
        return isCorrect(rgx, mobiles);
    }

    /**
     * 校验邮箱
     * @param email
     * @return
     */
    public static boolean isEmail(String email){
        String str="^([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)*@([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)+[\\.][A-Za-z]{2,3}([\\.][A-Za-z]{2})?$";
        return isCorrect(str,email);
    }

    //验证身份证号码
    public static boolean isCardNumber(String number)
    {
        String rgx = "^\\d{15}|^\\d{17}([0-9]|X|x)$";
        return isCorrect(rgx, number);
    }

    //正则验证
    public static boolean isCorrect(String rgx, String res)
    {
        if(StringUtils.isEmpty(res)){
            return false;
        }
        Pattern p = Pattern.compile(rgx);
        Matcher m = p.matcher(res);
        return m.matches();
    }

    /**
     * 车牌号校验
     *
     * @param licenseNo
     * @return
     */
    public static boolean isLicenseNo(String licenseNo){
        if(StringUtils.isEmpty(licenseNo)){
            return false;
        }
        if(licenseNo.length()<5 || licenseNo.length()>10){
            return false;
        }
        String rgx = "^[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领A-Z]{1}[A-Z]{1}[A-Z0-9]{4}[A-Z0-9挂学警港澳]{1}$";
        return isCorrect(rgx, licenseNo);
    }

    public static String getSex(String applicantIdNo){
        try {
            if(StringUtils.isNoneBlank(applicantIdNo) && applicantIdNo.length()>2){
                String s = applicantIdNo.substring(applicantIdNo.length() - 2, applicantIdNo.length() - 1);
                if(Integer.parseInt(s)%2==0){
                    return  "女";
                }else{
                    return "男";
                }
            }
        }catch (Exception e){
            return "男";
        }
        return "男";
    }


    public static void main(String[] args) {
        //System.out.println(isLicenseNo("川AT951D"));
        System.out.println(isMobileNO("19912345678"));
    }
}
