package com.homvee.youhui.dao.acct;

import com.homvee.youhui.dao.acct.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Copyright (c) 2018$. ddyunf.com all rights reserved
 *
 * @author Homvee.Tang(tanghongwei @ ddcloudf.com)
 * @version V1.0
 * @Description TODO(用一句话描述该文件做什么)
 * @date 2018-08-16 11:07
 */
public interface AccountDao extends JpaRepository<Account, Long> , AccountDaoExt {
    /**
     * find by like name
     * @param acctName
     * @param userId
     * @param yn
     * @return
     */
    List<Account> findByAcctNameAndUserIdAndYn(String acctName, Long userId, Integer yn);

    /**
     * find
     * @param userId
     * @param val
     * @return
     */
    List<Account> findByUserIdAndYn(Long userId, Integer val);

    /**
     * del
     * @param ids
     * @return
     */
    @Modifying
    @Query(value = "update t_account set yn=0 ,changeTime=now() where id in(?1)" , nativeQuery = true)
    Integer delByIds(List<Long> ids);

    /**
     * find
     * @param acctName
     * @param yn
     * @return
     */
    List<Account> findByAcctNameLikeAndYn(String acctName, Integer yn);

    /**
     * find by yn
     * @param yn
     * @return
     */
    List<Account> findByYn(Integer yn);
}