package com.homvee.youhui.service.acct.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.homvee.youhui.common.enums.YNEnum;
import com.homvee.youhui.common.vos.AccountVO;
import com.homvee.youhui.common.vos.Pager;
import com.homvee.youhui.dao.acct.AccountDao;
import com.homvee.youhui.dao.acct.model.Account;
import com.homvee.youhui.service.BaseServiceImpl;
import com.homvee.youhui.service.acct.AccountService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

/**
 * Copyright (c) 2018$. ddyunf.com all rights reserved
 *
 * @author Homvee.Tang(tanghongwei @ ddcloudf.com)
 * @version V1.0
 * @Description TODO(用一句话描述该文件做什么)
 * @date 2018-08-17 13:45
 */
@Service("accountService")
public class AccountServiceImpl extends BaseServiceImpl<Account,Long> implements AccountService {

    @Resource
    private AccountDao accountDao;

    @Override
    public List<Account> save(List<Account> accts) {
        return accountDao.saveAll(accts);
    }

    @Override
    public List<Account> findByAcctNameAndUserId(String acctName, Long userId) {
        return accountDao.findByAcctNameAndUserIdAndYn(acctName, userId, YNEnum.YES.getVal());
    }

    @Override
    public Account findOne(Long id) {
        Optional<Account> optional = accountDao.findById(id);
        return optional != null && optional.isPresent() ? optional.get() : null;
    }

    @Override
    public Pager findByConditions(AccountVO accountVO, Pager pager) {
        pager = accountDao.findByConditions(accountVO , pager);

        if (pager != null && !CollectionUtils.isEmpty(pager.getData())){
            List<AccountVO> vos = Lists.newArrayList();
            for (Object obj: pager.getData() ){
                String tmp = JSONObject.toJSONString(obj);
                AccountVO vo = JSON.toJavaObject(JSONObject.parseObject(tmp) , AccountVO.class);
                vos.add(vo);
            }
            pager.setData(vos);
        }

        return pager;
    }

    @Override
    public List<Account> findByUserId(Long userId) {
        return accountDao.findByUserIdAndYn(userId, YNEnum.YES.getVal());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delByIds(Long[] ids) {
        accountDao.delByIds(Lists.newArrayList(ids));
    }

    @Override
    public List<Account> findByAcctName(String acctName) {
        return accountDao.findByAcctNameLikeAndYn(acctName , YNEnum.YES.getVal());
    }

    @Override
    public List<Account> findAll() {
        return accountDao.findByYn(YNEnum.YES.getVal());
    }
}
