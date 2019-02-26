package test.com.homve.livebroadcast.service;

import com.homvee.youhui.common.components.RedisComponent;
import org.junit.Test;
import test.com.homve.BaseTest;

import javax.annotation.Resource;

/**
 * Copyright (c) 2018$. ddyunf.com all rights reserved
 *
 * @author Homvee.Tang(tanghongwei @ ddcloudf.com)
 * @version V1.0
 * @Description TODO(用一句话描述该文件做什么)
 * @date 2018-09-19 11:05
 */
public class RedisComponentTest extends BaseTest {

    @Resource
    private RedisComponent redisComponent;

    @Test
    public void addZSet() {
//        redisComponent.addZSet("test" , "A" , System.currentTimeMillis());
//        redisComponent.addZSet("test" , "B" , System.currentTimeMillis());
//        redisComponent.addZSet("test" , "C" , System.currentTimeMillis());
//        redisComponent.addZSet("test" , "D" , System.currentTimeMillis());
//        getZSetRank();
//        System.out.println(redisComponent.getZSetVal("test" , 0L , Long.valueOf(Integer.MAX_VALUE)));
        redisComponent.setStrNx("test" , 90L);
        System.out.println(redisComponent.getStrNx("test"));
    }

    @Test
    public void getZSetRank() {
        System.out.println(redisComponent.getZSetRank("test" , "A"));
        System.out.println(redisComponent.getZSetRank("test" , "B"));
        System.out.println(redisComponent.getZSetRank("test" , "C"));
        System.out.println(redisComponent.getZSetRank("test" , "D"));
    }

    @Test
    public void incrZSetScore() {
    }
}