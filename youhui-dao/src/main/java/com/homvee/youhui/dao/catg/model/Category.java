package com.homvee.youhui.dao.catg.model;

import com.homvee.youhui.dao.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Copyright (c) 2018$. ddyunf.com all rights reserved
 *
 * @author Homvee.Tang(tanghongwei @ ddcloudf.com)
 * @version V1.0
 * @Description TODO(用一句话描述该文件做什么)
 * @date 2018-08-16 11:14
 */
@Entity
@Table(name = "t_category")
public class Category extends BaseEntity {

    private String catgName;

    private Long parentId;

    private Long userId;

    public String getCatgName() {
        return catgName;
    }

    public void setCatgName(String catgName) {
        this.catgName = catgName;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
