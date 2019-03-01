package com.homvee.youhui.dao.discount.impl;

import com.google.common.collect.Maps;
import com.homvee.youhui.common.vos.DiscountVo;
import com.homvee.youhui.common.vos.Pager;
import com.homvee.youhui.dao.JpaDaoSupport;
import com.homvee.youhui.dao.discount.DiscountDaoExt;
import com.homvee.youhui.dao.discount.model.Discount;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class DiscountDaoExtImpl extends JpaDaoSupport<Discount, Long> implements DiscountDaoExt {


    @Override
    public Pager findByCondtion(DiscountVo discountVo) {
        Map<String , Object> params = Maps.newHashMap();
        StringBuffer sql = new StringBuffer("SELECT d.id,d.dname,d.discountType,d.remark,d.detail,v.vname,v.mobile,v.addr,v.logoUrl FROM `discount` d LEFT JOIN `vender` v ON d.venderId=v.id WHERE d.online=1  ");
        if(discountVo.getDiscountType()!=null){
            sql.append(" and d.discountType = :discountType");
            params.put("discountType",discountVo.getDiscountType());
        }

        ///////////////结束//////////////
        sql.append(" order by d.createTime desc ");
        LOGGER.info("DiscountDaoExtImpl.findByCondtion拼接的SQL=[{}] ,参数[{}]" ,sql , params);

        Pager pager = null;
        try {
            pager = this.doSQLPage(sql.toString() , params , new HashMap<>().getClass(),discountVo.getPageNum() ,discountVo.getPageSize());
        } catch (Exception e) {
            LOGGER.info("DiscountDaoExtImpl.findByConditions拼接的SQL=[{}] ,参数[{}]异常" ,sql , params  ,e);
            throw new RuntimeException(e);
        }
        return pager;
    }
}
