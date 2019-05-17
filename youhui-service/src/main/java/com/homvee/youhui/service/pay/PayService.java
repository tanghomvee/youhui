package com.homvee.youhui.service.pay;

import com.homvee.youhui.common.vos.Msg;

import java.util.Map;

public interface PayService {

    Msg findPayInfo(String openid);

    Msg createCharge(String openid);

    void callBack(Map<String, String> notifyMap);
}
