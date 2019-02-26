package com.homvee.youhui.dao.room;

import com.homvee.youhui.dao.room.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Copyright (c) 2018$. ddyunf.com all rights reserved
 *
 * @author Homvee.Tang(tanghongwei@ddcloudf.com)
 * @version V1.0
 * @Description Network Interface Card Dao
 * @date 2018-04-16 17:42
 */
public interface RoomDao extends JpaRepository<Room, Long> , RoomDaoExt{


    /**
     * find by url
     * @param url
     * @param userId
     * @param yn
     * @return
     */
    List<Room> findByUrlAndUserIdAndYn(String url, Long userId, Integer yn);

    /**
     * find by like roomName
     * @param roomName
     * @param userId
     * @param yn
     * @return
     */
    List<Room> findByRoomNameContainingAndUserIdAndYn(String roomName, Long userId, Integer yn);

    /**
     * find by url
     * @param url
     * @param val
     * @return
     */
    Room findByUrlAndYn(String url, Integer val);

    /**
     * find by user Id
     * @param userId
     * @param yn
     * @return
     */
    List<Room> findByUserIdAndYn(Long userId, Integer yn);



    /**
     * del
     * @param ids
     * @return
     */
    @Modifying
    @Query(value = "update t_room set yn=0 ,changeTime=now() where id in(?1)" , nativeQuery = true)
    Integer delByIds(List<Long> ids);

    /**
     * find all
     * @param ways
     * @param yn
     * @return
     */
    List<Room> findByWayInAndYn(List<Integer> ways ,Integer yn);

    /**
     * find
     * @param acctId
     * @param userId
     * @return
     */
    @Query(value = "select * from t_room where yn=1 and userId=?2 and way !=3 and id in(select roomId from t_content where yn=1 and userId=?2 and acctId=?1)" , nativeQuery = true)
    List<Room> findByAcctIdAndUserId(Long acctId, Long userId);

    /**
     * find
     * @param way
     * @param hourOfDay
     * @return
     */
    @Query(value = "select * from t_room where yn=1 and  way=?1 and startHour <=?2 and ?2 <= endHour" , nativeQuery = true)
    List<Room> findByWayAndHour(Integer way, int hourOfDay);
}
