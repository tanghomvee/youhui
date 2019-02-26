package com.homvee.youhui.service.acct;

import com.homvee.youhui.common.vos.AccountVO;
import com.homvee.youhui.common.vos.Pager;
import com.homvee.youhui.dao.acct.model.Account;
import com.homvee.youhui.service.BaseService;

import java.util.List;

/**
 * Copyright (c) 2018$. ddyunf.com all rights reserved
 *
 * @author Homvee.Tang(tanghongwei @ ddcloudf.com)
 * @version V1.0
 * @Description TODO(用一句话描述该文件做什么)
 * @date 2018-08-16 10:26
 */
public interface AccountService extends BaseService<Account, Long> {

    /**
     *
     * @param accts
     * @return
     */
    List<Account> save(List<Account> accts);


    /**
     * find by name
     * @param acctName
     * @param userId
     * @return
     */
    List<Account> findByAcctNameAndUserId(String acctName, Long userId);

    /**
     * find by id
     * @param id
     * @return
     */
    Account findOne(Long id);


    /**
     * find page
     * @param accountVO
     * @param pager
     * @return
     */
    Pager findByConditions(AccountVO accountVO, Pager pager);

    /**
     * find by userId
     * @param userId
     * @return
     */
    List<Account> findByUserId(Long userId);

    /**
     * del
     * @param ids
     */
    void delByIds(Long[] ids);

    /**
     * find
     * @param acctName
     * @return
     */
    List<Account> findByAcctName(String acctName);

    /**
     * find  valid all
     * @return
     */
    List<Account> findAll();
}
