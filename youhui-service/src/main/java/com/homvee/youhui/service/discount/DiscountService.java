package com.homvee.youhui.service.discount;

import com.homvee.youhui.common.vos.DiscountVo;
import com.homvee.youhui.common.vos.Msg;

public interface DiscountService {

    Msg findByCondition(DiscountVo discountVo);

    Msg detail(Long id, String openId);
}
