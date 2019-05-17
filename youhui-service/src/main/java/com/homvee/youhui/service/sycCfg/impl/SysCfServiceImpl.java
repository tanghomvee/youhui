package com.homvee.youhui.service.sycCfg.impl;

import com.homvee.youhui.dao.cfg.SysCfgDao;
import com.homvee.youhui.dao.cfg.model.SysCfg;
import com.homvee.youhui.service.sycCfg.SysCfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("sysCfService")
public class SysCfServiceImpl implements SysCfService {


    @Autowired
    private SysCfgDao sysCfgDao;

    @Override
    public SysCfg findByCodeAndYn(String code, Integer yn) {
        return sysCfgDao.findByCodeAndYn(code,yn);
    }
}
