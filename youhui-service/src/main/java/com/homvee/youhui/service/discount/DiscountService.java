package com.homvee.youhui.service.discount;

import com.homvee.youhui.common.vos.DiscountVo;
import com.homvee.youhui.common.vos.Msg;

import java.util.List;

public interface DiscountService {

    Msg findByCondition(DiscountVo discountVo);

    Msg detail(Long id, String openId);

    Msg typeList();
}
