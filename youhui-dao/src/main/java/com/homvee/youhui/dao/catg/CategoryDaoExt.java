package com.homvee.youhui.dao.catg;

import com.homvee.youhui.common.vos.CategoryVO;
import com.homvee.youhui.common.vos.Pager;

/**
 * Copyright (c) 2018$. ddyunf.com all rights reserved
 *
 * @author Homvee.Tang(tanghongwei @ ddcloudf.com)
 * @version V1.0
 * @Description TODO(用一句话描述该文件做什么)
 * @date 2018-08-16 11:14
 */
public interface CategoryDaoExt{
    /**
     * find by page
     * @param categoryVO
     * @param pager
     * @return
     */
    Pager findByConditions(CategoryVO categoryVO, Pager pager);
}
