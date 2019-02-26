package com.homvee.youhui.dao.sms;


import com.homvee.youhui.dao.sms.model.SendingSMS;
import org.springframework.data.jpa.repository.JpaRepository;


public interface SendingSMSDao extends JpaRepository<SendingSMS, Long> , SendingSMSDaoExt {

}
