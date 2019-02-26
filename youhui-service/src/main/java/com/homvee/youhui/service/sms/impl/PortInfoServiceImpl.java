package com.homvee.youhui.service.sms.impl;

import com.homvee.youhui.common.enums.YNEnum;
import com.homvee.youhui.dao.sms.PortInfoDao;
import com.homvee.youhui.dao.sms.model.PortInfo;
import com.homvee.youhui.service.BaseServiceImpl;
import com.homvee.youhui.service.sms.PortInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("portInfoService")
public class PortInfoServiceImpl extends BaseServiceImpl<PortInfo ,Long> implements PortInfoService {
    @Resource
    private PortInfoDao portInfoDao;

    @Override
    public PortInfo findByPhonNum(String phoneNum) {
        return portInfoDao.findByPhoNumAndYn(phoneNum , YNEnum.YES.getVal());
    }

    @Override
    public PortInfo findRandPhoneNum() {
        return portInfoDao.findRandPhoneNum();
    }
}
