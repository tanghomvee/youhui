package com.homvee.youhui.dao.content;

import com.homvee.youhui.dao.content.model.Content;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.math.BigInteger;
import java.util.List;

/**
 * Copyright (c) 2018$. ddyunf.com all rights reserved
 *
 * @author Homvee.Tang(tanghongwei @ ddcloudf.com)
 * @version V1.0
 * @Description TODO(用一句话描述该文件做什么)
 * @date 2018-08-16 11:02
 */
public interface ContentDao extends JpaRepository<Content, Long>,ContentDaoExt {

    /**
     * find
     * @param roomId
     * @param userId
     * @return
     */
    @Query(value = " select * from t_content where yn=1 and roomId=?1 and userId=?2 order by recentUsedTime limit 1" , nativeQuery = true)
    Content findByRoomIdAndUserId(Long roomId, Long userId);

    /**
     * find
     * @param roomId
     * @param userId
     * @param accountId
     * @return
     */
    @Query(value = " select * from t_content where yn=1 and roomId=?1 and userId=?2 and acctId=?3 and used=0 order by id limit 1" , nativeQuery = true)
    Content findByRoomIdAndUserIdAndAndNotUsed(Long roomId, Long userId, Long accountId);
    /**
     * find
     * @param roomId
     * @param userId
     * @param accountId
     * @return
     */
    @Query(value = " select * from t_content where yn=1 and roomId=?1 and userId=?2 and acctId=?3 and used=1 order by id limit 1" , nativeQuery = true)
    Content findByRoomIdAndUserIdAndAndUsed(Long roomId, Long userId, Long accountId);

    /**
     * cnt
     * @param roomId
     * @return
     */
    @Query(value = " select count(id) from t_content where yn=1 and roomId=?1  and used=1" , nativeQuery = true)
    Integer countUsedByRoomId(Long roomId);

    /**
     * cnt
     * @param roomId
     * @return
     */
    @Query(value = " select count(id) from t_content where yn=1 and roomId=?1 " , nativeQuery = true)
    Integer countByRoomId(Long roomId);

    /**
     * reset state
     * @param roomId
     * @return
     */
    @Modifying
    @Query(value = " update t_content set used=0  where yn=1 and roomId=?1 " , nativeQuery = true)
    Integer resetUsed(Long roomId);

    /**
     * find
     * @param roomId
     * @return
     */
    @Query(value = " select acctId from t_content where yn=1 and roomId=?1 " , nativeQuery = true)
    List<BigInteger> findAcctByRoomId(Long roomId);

    /**
     * find
     * @param roomId
     * @param acctId
     * @param userId
     * @return
     */
    @Query(value = " select * from t_content where yn=1 and roomId=?1 AND acctId=?2 AND userId=?3" , nativeQuery = true)
    List<Content> findByRoomIdAndAcctIdAndUserId(Long roomId, Long acctId , Long userId);

    /**
     * find
     * @param yn
     * @return
     */
    List<Content> findByYnOrderByRoomId(Integer yn);

    /**
     * find
     * @param roomId
     * @param yn
     * @return
     */
    List<Content> findByRoomIdAndYn(Long roomId, Integer yn);

    /**
     * find
     * @param acctId
     * @param roomId
     * @param yn
     * @return
     */
    List<Content> findByAcctIdAndRoomIdAndYn(Long acctId, Long roomId, Integer yn);
}
