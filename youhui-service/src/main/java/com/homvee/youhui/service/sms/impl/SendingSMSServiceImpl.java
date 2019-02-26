package com.homvee.youhui.service.sms.impl;

import com.homvee.youhui.common.components.RedisComponent;
import com.homvee.youhui.common.constants.RedisKey;
import com.homvee.youhui.common.enums.SeparatorEnum;
import com.homvee.youhui.dao.sms.SendingSMSDao;
import com.homvee.youhui.dao.sms.model.SendingSMS;
import com.homvee.youhui.service.sms.SendingSMSService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("sendingSMSService")
public class SendingSMSServiceImpl implements SendingSMSService {

    @Resource
    private SendingSMSDao sendingSMSDao;
    @Resource
    private RedisComponent redisComponent;

    @Override
    public Long saveSendingSMS(String mobile, String content) {
        return sendingSMSDao.saveSendingSMS(mobile ,content);
    }

    @Override
    public SendingSMS save(SendingSMS sendingSMS, Long timeout) {
        sendingSMS.setSmsState(0);
        String val = RedisKey.SMS_SENDING_EXIST +SeparatorEnum.UNDERLINE.getVal()+ sendingSMS.getPhoNum() + SeparatorEnum.UNDERLINE.getVal() + sendingSMS.getSmsNumber() + SeparatorEnum.UNDERLINE.getVal() + sendingSMS.getSmsContent();
        if(!redisComponent.setStrNx( val ,  timeout)){
            return sendingSMS;
        }
        return sendingSMSDao.save(sendingSMS);
    }
}
