package com.homvee.youhui.service.sms;

import com.homvee.youhui.dao.sms.model.SendingSMS;

public interface SendingSMSService {
    /**
     * save
     * @param mobile
     * @param content
     * @return
     */
    Long saveSendingSMS(String mobile, String content);

    /**
     * save
     * @param sendingSMS
     * @param timeout
     * @return
     */
    SendingSMS save(SendingSMS sendingSMS, Long timeout);
}
