package com.homvee.youhui.dao.discount;

import com.homvee.youhui.common.vos.DiscountVo;
import com.homvee.youhui.common.vos.Pager;

import java.util.Map;

public interface DiscountDaoExt {

    Pager findByCondtion(DiscountVo discountVo);

}
