package com.homvee.youhui.dao.acct;

import com.homvee.youhui.common.vos.AccountVO;
import com.homvee.youhui.common.vos.Pager;

/**
 * Copyright (c) 2018$. ddyunf.com all rights reserved
 *
 * @author Homvee.Tang(tanghongwei @ ddcloudf.com)
 * @version V1.0
 * @Description TODO(用一句话描述该文件做什么)
 * @date 2018-08-16 11:07
 */
public interface AccountDaoExt {
    /**
     * find by page
     * @param accountVO
     * @param pager
     * @return
     */
    Pager findByConditions(AccountVO accountVO, Pager pager);
}