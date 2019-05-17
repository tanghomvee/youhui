package com.homvee.youhui.dao.pay;

import com.homvee.youhui.dao.pay.model.PayInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PayInfoDao extends JpaRepository<PayInfo, Long> {
    PayInfo findByOrderId(String orderId);
}
