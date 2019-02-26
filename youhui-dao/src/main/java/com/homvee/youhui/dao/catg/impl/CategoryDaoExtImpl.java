package com.homvee.youhui.dao.catg.impl;

import com.google.common.collect.Maps;
import com.homvee.youhui.common.enums.YNEnum;
import com.homvee.youhui.common.vos.CategoryVO;
import com.homvee.youhui.common.vos.Pager;
import com.homvee.youhui.dao.JpaDaoSupport;
import com.homvee.youhui.dao.catg.CategoryDaoExt;
import com.homvee.youhui.dao.catg.model.Category;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Copyright (c) 2018$. ddyunf.com all rights reserved
 *
 * @author Homvee.Tang(tanghongwei @ ddcloudf.com)
 * @version V1.0
 * @Description TODO(用一句话描述该文件做什么)
 * @date 2018-08-17 14:26
 */
public class CategoryDaoExtImpl extends JpaDaoSupport<Category,Long> implements CategoryDaoExt {
    @Override
    public Pager findByConditions(CategoryVO categoryVO, Pager pager) {
        StringBuffer sql = new StringBuffer("SELECT ta.* , tu.userName,ta1.catgName AS parentCatgName FROM t_category ta   ");
        sql.append(" left join t_user tu on tu.id = ta.userId");
        sql.append(" left join t_category ta1 on ta1.id = ta.parentId");
        sql.append("   where ta.yn=:yn  AND tu.yn=1  ");

        Map<String , Object> params = Maps.newHashMap();
        params.put("yn" , YNEnum.YES.getVal());
        if (!StringUtils.isEmpty(categoryVO.getCatgName())){
            sql.append(" AND ta.catgName like :catgName");
            params.put("catgName" , "%" + categoryVO.getCatgName() + "%");
        }
        if (!StringUtils.isEmpty(categoryVO.getUserId())){
            sql.append(" AND ta.userId = :userId");
            params.put("userId" ,categoryVO.getUserId() );
        }
        if (!StringUtils.isEmpty(categoryVO.getParentId())){
            sql.append(" AND ta.parentId = :parentId");
            params.put("parentId" ,categoryVO.getParentId() );
        }
        sql.append("  order by   ta.createTime desc");

        try{

            Pager retPager = super.doSQLPage(sql.toString() , params , HashMap.class , pager.getPageNum() ,pager.getPageSize());
            return retPager;
        }catch (Exception ex){
            LOGGER.error("分页查询类目数据异常,sql={} ,params={}" ,sql , params ,ex);
        }
        return null;
    }
}
