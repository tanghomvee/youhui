package com.homvee.youhui.service.catg.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.homvee.youhui.common.enums.YNEnum;
import com.homvee.youhui.common.vos.CategoryVO;
import com.homvee.youhui.common.vos.Pager;
import com.homvee.youhui.dao.catg.CategoryDao;
import com.homvee.youhui.dao.catg.model.Category;
import com.homvee.youhui.service.BaseServiceImpl;
import com.homvee.youhui.service.catg.CategoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

/**
 * Copyright (c) 2018$. ddyunf.com all rights reserved
 *
 * @author Homvee.Tang(tanghongwei @ ddcloudf.com)
 * @version V1.0
 * @Description TODO(用一句话描述该文件做什么)
 * @date 2018-08-17 14:24
 */
@Service("categoryService")
public class CategoryServiceImpl extends BaseServiceImpl<Category , Long> implements CategoryService {

    @Resource
    private CategoryDao categoryDao;

    @Override
    public List<Category> save(List<Category> categories) {
        return categoryDao.saveAll(categories);
    }

    @Override
    public Category findOne(Long id) {
        Optional<Category> optional = categoryDao.findById(id);
        return optional != null && optional.isPresent() ? optional.get() : null;
    }

    @Override
    public Pager findByConditions(CategoryVO categoryVO, Pager pager) {
        pager = categoryDao.findByConditions(categoryVO , pager);
        if (pager != null && !CollectionUtils.isEmpty(pager.getData())){
            List<CategoryVO> vos = Lists.newArrayList();
            for (Object obj: pager.getData() ){
                String tmp = JSONObject.toJSONString(obj);
                CategoryVO vo = JSON.toJavaObject(JSONObject.parseObject(tmp) , CategoryVO.class);
                vos.add(vo);
            }
            pager.setData(vos);
        }

        return pager;
    }

    @Override
    public List<Category> findByParentIdAndUserId(Long parentId, Long userId) {
        if (parentId == null){
            return categoryDao.findByParentIdIsNullAndUserIdAndYn(userId,YNEnum.YES.getVal());
        }
        return  categoryDao.findByParentIdAndUserIdAndYn(parentId,userId,YNEnum.YES.getVal());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Integer delByIds(Long[] ids) {
        return  categoryDao.delByIds(Lists.newArrayList(ids));
    }
}
