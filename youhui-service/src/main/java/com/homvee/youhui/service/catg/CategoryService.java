package com.homvee.youhui.service.catg;

import com.homvee.youhui.common.vos.CategoryVO;
import com.homvee.youhui.common.vos.Pager;
import com.homvee.youhui.dao.catg.model.Category;
import com.homvee.youhui.service.BaseService;

import java.util.List;

/**
 * Copyright (c) 2018$. ddyunf.com all rights reserved
 *
 * @author Homvee.Tang(tanghongwei @ ddcloudf.com)
 * @version V1.0
 * @Description TODO(用一句话描述该文件做什么)
 * @date 2018-08-16 10:26
 */
public interface CategoryService extends BaseService<Category, Long> {

    /**
     *
     * @param categories
     * @return
     */
    List<Category> save(List<Category> categories);


    /**
     * find by id
     * @param id
     * @return
     */
    Category findOne(Long id);


    /**
     * find page
     * @param categoryVO
     * @param pager
     * @return
     */
    Pager findByConditions(CategoryVO categoryVO, Pager pager);

    /**
     * findByParentIdAndUserId
     * @param parentId
     * @param userId
     * @return
     */
    List<Category> findByParentIdAndUserId(Long parentId, Long userId);

    /**
     * del
     * @param ids
     * @return
     */
    Integer delByIds(Long[] ids);
}
