package com.homvee.youhui.dao.discount;

import com.homvee.youhui.common.vos.DiscountVo;
import com.homvee.youhui.common.vos.Msg;
import com.homvee.youhui.common.vos.Pager;
import com.homvee.youhui.dao.discount.model.Discount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Map;

public interface DiscountDao extends JpaRepository<Discount, Long>,DiscountDaoExt {


}
