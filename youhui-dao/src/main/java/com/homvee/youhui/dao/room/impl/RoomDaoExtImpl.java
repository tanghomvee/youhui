package com.homvee.youhui.dao.room.impl;

import com.google.common.collect.Maps;
import com.homvee.youhui.common.enums.YNEnum;
import com.homvee.youhui.common.vos.Pager;
import com.homvee.youhui.common.vos.RoomVO;
import com.homvee.youhui.dao.JpaDaoSupport;
import com.homvee.youhui.dao.room.RoomDaoExt;
import com.homvee.youhui.dao.room.model.Room;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Copyright (c) 2018$. ddyunf.com all rights reserved
 *
 * @author Homvee.Tang(tanghongwei @ ddcloudf.com)
 * @version V1.0
 * @Description TODO(用一句话描述该文件做什么)
 * @date 2018-08-16 09:50
 */
public class RoomDaoExtImpl extends JpaDaoSupport<Room,Long> implements RoomDaoExt {
    @Override
    public Pager findByConditions(RoomVO roomVO, Pager pager) {
        StringBuffer sql = new StringBuffer("SELECT tr.*,tu.userName FROM t_room tr ");
        sql.append(" left join t_user tu on tu.id = tr.userId");
        sql.append("   where tr.yn=:yn  AND tu.yn=1  ");

        Map<String , Object> params = Maps.newHashMap();
        params.put("yn" , YNEnum.YES.getVal());
        if (!StringUtils.isEmpty(roomVO.getRoomName())){
            sql.append(" AND tr.roomName like :roomName");
            params.put("roomName" , "%" + roomVO.getRoomName() + "%");
        }
        if (!StringUtils.isEmpty(roomVO.getUrl())){
            sql.append(" AND tr.url like :url");
            params.put("url" ,"%" + roomVO.getUrl() + "%");
        }
        if (!StringUtils.isEmpty(roomVO.getUserId())){
            sql.append(" AND tr.userId = :userId");
            params.put("userId" ,roomVO.getUserId() );
        }
        if (!StringUtils.isEmpty(roomVO.getWay())){
            sql.append(" AND tr.way = :way");
            params.put("way" ,roomVO.getWay() );
        }
        sql.append(" order by   tr.createTime desc");

        try{

            Pager retPager = super.doSQLPage(sql.toString() , params , HashMap.class , pager.getPageNum() ,pager.getPageSize());
            return retPager;
        }catch (Exception ex){
            LOGGER.error("分页查询直播间数据异常,sql={} ,params={}" ,sql , params ,ex);
        }
        return null;
    }
}
