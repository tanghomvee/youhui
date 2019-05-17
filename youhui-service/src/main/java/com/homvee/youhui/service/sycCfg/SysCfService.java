package com.homvee.youhui.service.sycCfg;

import com.homvee.youhui.dao.cfg.model.SysCfg;

public interface SysCfService {

    SysCfg findByCodeAndYn(String code, Integer yn);
}
