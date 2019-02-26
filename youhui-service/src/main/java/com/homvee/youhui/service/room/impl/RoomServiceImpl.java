package com.homvee.youhui.service.room.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.homvee.youhui.common.enums.YNEnum;
import com.homvee.youhui.common.vos.Pager;
import com.homvee.youhui.common.vos.RoomVO;
import com.homvee.youhui.dao.room.RoomDao;
import com.homvee.youhui.dao.room.model.Room;
import com.homvee.youhui.service.BaseServiceImpl;
import com.homvee.youhui.service.room.RoomService;
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
 * @date 2018-08-16 10:27
 */
@Service("roomService")
public class RoomServiceImpl extends BaseServiceImpl<Room ,Long> implements RoomService {
    @Resource
    private RoomDao roomDao;

    @Override
    public List<Room> save(List<Room> rooms) {
        return roomDao.saveAll(rooms);
    }

    @Override
    public List<Room> findByUrlAndUserId(String url, Long userId) {
        return roomDao.findByUrlAndUserIdAndYn(url,userId , YNEnum.YES.getVal());
    }

    @Override
    public List<Room> findByRoomNameAndUserId(String roomName, Long userId) {
        return roomDao.findByRoomNameContainingAndUserIdAndYn(roomName,userId , YNEnum.YES.getVal());
    }

    @Override
    public Room findOne(Long id) {
        Optional<Room> optional = roomDao.findById(id);
        return optional != null && optional.isPresent() ? optional.get() : null;
    }

    @Override
    public Pager findByConditions(RoomVO roomVO, Pager pager) {
        pager = roomDao.findByConditions(roomVO , pager);
        if (pager != null && !CollectionUtils.isEmpty(pager.getData())){
            List<RoomVO> vos = Lists.newArrayList();
            for (Object obj: pager.getData() ){
                String tmp = JSONObject.toJSONString(obj);
                RoomVO vo = JSON.toJavaObject(JSONObject.parseObject(tmp) , RoomVO.class);
                vos.add(vo);
            }
            pager.setData(vos);
        }

        return pager;
    }

    @Override
    public Room findByUrl(String url) {
        return roomDao.findByUrlAndYn(url , YNEnum.YES.getVal());
    }

    @Override
    public List<Room> findByUserId(Long userId) {
        return roomDao.findByUserIdAndYn(userId , YNEnum.YES.getVal());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Integer delByIds(Long[] ids) {
        return  roomDao.delByIds(Lists.newArrayList(ids));
    }

    @Override
    public List<Room> findByWay(Integer ... ways) {
        return roomDao.findByWayInAndYn(Lists.newArrayList(ways) ,YNEnum.YES.getVal());
    }

    @Override
    public List<Room> findByAcctIdAndUserId(Long acctId, Long userId) {
        return roomDao.findByAcctIdAndUserId(acctId , userId);
    }

    @Override
    public List<Room> findByWayAndHour(Integer way, int hourOfDay) {
        return roomDao.findByWayAndHour(way , hourOfDay);
    }
}
