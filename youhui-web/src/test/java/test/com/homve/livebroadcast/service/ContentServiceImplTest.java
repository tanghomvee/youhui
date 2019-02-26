package test.com.homve.livebroadcast.service;

import com.homvee.youhui.dao.content.model.Content;
import com.homvee.youhui.service.content.ContentService;
import org.junit.Test;
import test.com.homve.BaseTest;

import javax.annotation.Resource;

/**
 * Copyright (c) 2018$. ddyunf.com all rights reserved
 *
 * @author Homvee.Tang(tanghongwei @ ddcloudf.com)
 * @version V1.0
 * @Description TODO(用一句话描述该文件做什么)
 * @date 2018-09-06 14:18
 */
public class ContentServiceImplTest extends BaseTest {

    @Resource
    private ContentService contentService;
    @Test
    public void save() {
    }

    @Test
    public void findOne() {
    }

    @Test
    public void findByConditions() {
    }

    @Test
    public void nextContent() {
    }

    @Test
    public void nextContent1() {
    }

    @Test
    public void delByIds() {
    }

    @Test
    public void loopContent() {
        Content content = contentService.loopContent(2L,1L,3L);
        System.out.println(content);
    }
}