package com.homvee.youhui.dao.sms;


import com.homvee.youhui.dao.sms.model.PortInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface PortInfoDao extends JpaRepository<PortInfo, Long>  {

    @Query(value = "select * from port_info where phoNum=?1 AND yn=?2 order by rand()  limit 1" , nativeQuery = true)
    PortInfo findByPhoNumAndYn(String phoneNum, Integer yn);

    @Query(value = "select * from port_info where yn=1 order by rand()  limit 1" , nativeQuery = true)
    PortInfo findRandPhoneNum();
}
