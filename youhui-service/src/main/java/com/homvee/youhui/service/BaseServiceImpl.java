package com.homvee.youhui.service;

import com.homvee.youhui.common.vos.Pager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;

import java.io.Serializable;

/**
 *
 * @author Administrator
 * @date 2017/7/14
 */
public class BaseServiceImpl<T  , PK extends Serializable> implements BaseService<T, PK> {
    protected Logger LOGGER = LoggerFactory.getLogger(this.getClass());

//    @Resource(name = "redisTemplate")
//    protected RedisTemplate<String, String> redisTemplate;
//    @Resource(name = "slaveRedisTemplate")
//    protected RedisTemplate<String, String> slaveRedisTemplate;

    @Override
    public Pager convertPage2Pager(Page page){
        Pager pager = new Pager(Long.valueOf(page.getTotalElements()).intValue() , page.getContent());
        return pager;
    }


}
