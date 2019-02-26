package com.homvee.youhui.service.content;

import com.homvee.youhui.common.vos.ContentVO;
import com.homvee.youhui.common.vos.Pager;
import com.homvee.youhui.dao.acct.model.Account;
import com.homvee.youhui.dao.content.model.Content;
import com.homvee.youhui.service.BaseService;

import java.math.BigInteger;
import java.util.List;

/**
 * Copyright (c) 2018$. ddyunf.com all rights reserved
 *
 * @author Homvee.Tang(tanghongwei @ ddcloudf.com)
 * @version V1.0
 * @Description TODO(用一句话描述该文件做什么)
 * @date 2018-08-16 10:26
 */
public interface ContentService extends BaseService<Content, Long> {

    /**
     *
     * @param contents
     * @return
     */
    List<Content> save(List<Content> contents);


    /**
     * find by id
     * @param id
     * @return
     */
    Content findOne(Long id);


    /**
     * find page
     * @param contentVO
     * @param pager
     * @return
     */
    Pager findByConditions(ContentVO contentVO, Pager pager);

    /**
     * find recently content
     * @param roomId
     * @param userId
     * @param account
     * @return
     */
    Content autoContent(Long roomId, Long userId, Account account);

    /**
     * find
     * @param id
     * @param userId
     * @param accountId
     * @return
     */
    Content nextContent(Long id, Long userId, Long accountId);

    /**
     * del
     * @param ids
     */
    void delByIds(Long[] ids);

    /**
     * 循环去对话
     * @param roomId
     * @param userId
     * @param acctId
     * @return
     */
    Content loopContent(Long roomId, Long userId, Long acctId);

    /**
     * find acctId by room id
     * @param roomId
     * @return
     */
    List<BigInteger> findAcctByRoomId(Long roomId);

    /**
     * find all
     * @return
     */
    List<Content> findAll();

    /**
     * find
     * @param roomId
     * @return
     */
    List<Content> findByRoomId(Long roomId);

    /**
     * find
     * @param acctId
     * @param roomId
     * @return
     */
    List<Content> findByAcctIdAndRoomId(Long acctId, Long roomId);
}
