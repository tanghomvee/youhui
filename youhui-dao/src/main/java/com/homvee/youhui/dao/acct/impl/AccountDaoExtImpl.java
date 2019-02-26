package com.homvee.youhui.dao.acct.impl;

import com.google.common.collect.Maps;
import com.homvee.youhui.common.enums.YNEnum;
import com.homvee.youhui.common.vos.AccountVO;
import com.homvee.youhui.common.vos.Pager;
import com.homvee.youhui.dao.JpaDaoSupport;
import com.homvee.youhui.dao.acct.AccountDaoExt;
import com.homvee.youhui.dao.acct.model.Account;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Copyright (c) 2018$. ddyunf.com all rights reserved
 *
 * @author Homvee.Tang(tanghongwei @ ddcloudf.com)
 * @version V1.0
 * @Description TODO(用一句话描述该文件做什么)
 * @date 2018-08-17 14:17
 */
public class AccountDaoExtImpl extends JpaDaoSupport<Account,Long> implements AccountDaoExt {
    @Override
    public Pager findByConditions(AccountVO accountVO, Pager pager) {
        StringBuffer sql = new StringBuffer("SELECT ta.* , tu.userName FROM t_account ta   ");
        sql.append(" left join t_user tu on tu.id = ta.userId");
        sql.append("   where ta.yn=:yn  AND tu.yn=1  ");

        Map<String , Object> params = Maps.newHashMap();
        params.put("yn" , YNEnum.YES.getVal());
        if (!StringUtils.isEmpty(accountVO.getAcctName())){
            sql.append(" AND ta.acctName like :acctName");
            params.put("acctName" , "%" + accountVO.getAcctName() + "%");
        }
        if (!StringUtils.isEmpty(accountVO.getMobile())){
            sql.append(" AND ta.mobile like :mobile");
            params.put("mobile" , "%" + accountVO.getMobile() + "%");
        }
        if (!StringUtils.isEmpty(accountVO.getUserId())){
            sql.append(" AND ta.userId = :userId");
            params.put("userId" ,accountVO.getUserId() );
        }
        sql.append("  order by   ta.createTime desc");

        try{

            Pager retPager = super.doSQLPage(sql.toString() , params , HashMap.class , pager.getPageNum() ,pager.getPageSize());
            return retPager;
        }catch (Exception ex){
            LOGGER.error("分页查询账号数据异常,sql={} ,params={}" ,sql , params ,ex);
        }
        return null;
    }
}
