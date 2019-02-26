package com.homvee.youhui.service.sms;

import com.homvee.youhui.dao.sms.model.PortInfo;
import com.homvee.youhui.service.BaseService;

public interface PortInfoService  extends BaseService<PortInfo, Long> {

    PortInfo findByPhonNum(String phoneNum);

    PortInfo findRandPhoneNum();
}

