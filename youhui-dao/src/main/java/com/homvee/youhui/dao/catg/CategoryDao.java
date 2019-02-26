package com.homvee.youhui.dao.catg;

import com.homvee.youhui.dao.catg.model.Category;
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
 * @date 2018-08-16 11:14
 */
public interface CategoryDao extends JpaRepository<Category, Long> , CategoryDaoExt{


    /**
     * find parent catg
     * @param userId
     * @param yn
     * @return
     */
    List<Category> findByParentIdIsNullAndUserIdAndYn(Long userId, Integer yn);

    /**
     * find children
     * @param parentId
     * @param userId
     * @param yn
     * @return
     */
    List<Category> findByParentIdAndUserIdAndYn(Long parentId, Long userId, Integer yn);

    /**
     * del
     * @param ids
     * @return
     */
    @Modifying
    @Query(value = "update t_category set yn=0 ,changeTime=now() where id in(?1)" , nativeQuery = true)
    Integer delByIds(List<Long> ids);
}
