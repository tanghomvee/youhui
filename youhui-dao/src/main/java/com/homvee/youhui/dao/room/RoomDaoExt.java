package com.homvee.youhui.dao.room;

import com.homvee.youhui.common.vos.Pager;
import com.homvee.youhui.common.vos.RoomVO;

/**
 * Copyright (c) 2018$. ddyunf.com all rights reserved
 *
 * @author Homvee.Tang(tanghongwei@ddcloudf.com)
 * @version V1.0
 * @Description Network Interface Card Dao
 * @date 2018-04-16 17:42
 */
public interface RoomDaoExt {


    /**
     * find by page
     * @param roomVO
     * @param pager
     * @return
     */
    Pager findByConditions(RoomVO roomVO, Pager pager);
}
