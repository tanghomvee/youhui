package com.homvee.youhui.common.sms;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SmsUtil {
    private static Logger LOGGER = LoggerFactory.getLogger(SmsUtil.class);

    private static final String accessKeyId = "LTAIfQLpYvJjUGBD";

    private static final String secret = "L2p2le2gyJaXIGrCvhEmVV9DANZGZI";

    public static boolean sendMsg(String mobile,String code){
        DefaultProfile profile = DefaultProfile.getProfile("default", accessKeyId, secret);
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        //request.setProtocol(ProtocolType.HTTPS);
        request.setMethod(MethodType.POST);
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        request.setAction("SendSms");

        request.putQueryParameter("PhoneNumbers", mobile);
        request.putQueryParameter("SignName", "爱宝盒");
        request.putQueryParameter("TemplateCode", "SMS_162110540");
        request.putQueryParameter("TemplateParam", "{\"code\":\""+code+"\"}");
        try {
            CommonResponse response = client.getCommonResponse(request);
            //{"Message":"OK","RequestId":"07D8D4D7-42A9-43C2-BF48-8BD44D1E0782","BizId":"460806653608743510^0","Code":"OK"}
            LOGGER.info("发送短信返回数据---->{}",response.getData());
            JSONObject retJson = JSONObject.parseObject(response.getData());
            if("OK".equals(retJson.getString("Code"))){
                return true;
            }
        } catch (Exception e) {
            LOGGER.error("发送短信失败--->{}",e);
            return false;
        }
        return false;
    }

    public static void main(String[] args) {
        System.out.println(sendMsg("13688132397","8889"));
    }
}
