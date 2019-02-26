package com.homvee.youhui.dao.sms.impl;

import com.homvee.youhui.dao.JpaDaoSupport;
import com.homvee.youhui.dao.sms.SendingSMSDaoExt;
import com.homvee.youhui.dao.sms.model.SendingSMS;

public class SendingSMSDaoExtImpl extends JpaDaoSupport<SendingSMS,Long> implements SendingSMSDaoExt {
    @Override
    public Long saveSendingSMS(String mobile, String content) {
        return null;
    }
}
