package com.homvee.youhui.dao.content;

import com.homvee.youhui.common.vos.ContentVO;
import com.homvee.youhui.common.vos.Pager;

/**
 * Copyright (c) 2018$. ddyunf.com all rights reserved
 *
 * @author Homvee.Tang(tanghongwei @ ddcloudf.com)
 * @version V1.0
 * @Description TODO(用一句话描述该文件做什么)
 * @date 2018-08-16 11:02
 */
public interface ContentDaoExt {
    /**
     * find by conditions
     * @param contentVO
     * @param pager
     * @return
     */
    Pager findByConditions(ContentVO contentVO, Pager pager);
}
