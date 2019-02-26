package com.homvee.youhui.dao.content.impl;

import com.google.common.collect.Maps;
import com.homvee.youhui.common.enums.YNEnum;
import com.homvee.youhui.common.vos.ContentVO;
import com.homvee.youhui.common.vos.Pager;
import com.homvee.youhui.dao.JpaDaoSupport;
import com.homvee.youhui.dao.content.ContentDaoExt;
import com.homvee.youhui.dao.content.model.Content;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Copyright (c) 2018$. ddyunf.com all rights reserved
 *
 * @author Homvee.Tang(tanghongwei @ ddcloudf.com)
 * @version V1.0
 * @Description TODO(用一句话描述该文件做什么)
 * @date 2018-08-17 14:36
 */
public class ContentDaoExtImpl extends JpaDaoSupport<Content,Long> implements ContentDaoExt {
    @Override
    public Pager findByConditions(ContentVO contentVO, Pager pager) {
        StringBuffer sql = new StringBuffer("SELECT tc.* , ta.acctName , tca.catgName,tu.userName,tr.roomName FROM t_content tc ");
        sql.append(" left join t_account ta on ta.id = tc.acctId");
        sql.append(" left join t_category tca on tca.id = tc.catgId");
        sql.append(" left join t_user tu on tu.id = tc.userId");
        sql.append(" left join t_room tr on tr.id = tc.roomId");
        sql.append("   where tc.yn=:yn AND ta.yn=1 AND tca.yn=1 AND tu.yn=1 AND tr.yn=1 ");
        Map<String , Object> params = Maps.newHashMap();
        params.put("yn" , YNEnum.YES.getVal());
        if (!StringUtils.isEmpty(contentVO.getContent())){
            sql.append(" AND tc.content like :content");
            params.put("content" , "%" + contentVO.getContent() + "%");
        }

        if (!StringUtils.isEmpty(contentVO.getUserId())){
            sql.append(" AND tc.userId = :userId");
            params.put("userId" ,contentVO.getUserId() );
        }
        if (!StringUtils.isEmpty(contentVO.getAcctId())){
            sql.append(" AND tc.acctId = :acctId");
            params.put("acctId" ,contentVO.getAcctId() );
        }
        if (!StringUtils.isEmpty(contentVO.getCatgId())){
            sql.append(" AND tc.catgId = :catgId");
            params.put("tc.catgId" ,contentVO.getCatgId() );
        }
        if (!StringUtils.isEmpty(contentVO.getRoomId())){
            sql.append(" AND tc.roomId = :roomId");
            params.put("roomId" ,contentVO.getRoomId() );
        }



        sql.append(" order by used ,createTime desc");

        try{

            Pager retPager = super.doSQLPage(sql.toString() , params , HashMap.class , pager.getPageNum() ,pager.getPageSize());
            return retPager;
        }catch (Exception ex){
            LOGGER.error("分页查询直内容数据异常,sql={} ,params={}" ,sql , params ,ex);
        }
        return null;
    }
}
