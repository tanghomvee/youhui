package com.homvee.youhui.dao.pay;

import com.homvee.youhui.dao.pay.model.Charge;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChargeDao extends JpaRepository<Charge, Long> {
}
