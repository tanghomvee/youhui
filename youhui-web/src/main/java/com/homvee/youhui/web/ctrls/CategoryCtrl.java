package com.homvee.youhui.web.ctrls;

import com.google.common.collect.Lists;
import com.homvee.youhui.dao.catg.model.Category;
import com.homvee.youhui.service.catg.CategoryService;
import com.homvee.youhui.web.BaseCtrl;
import com.homvee.youhui.common.vos.BaseVO;
import com.homvee.youhui.common.vos.CategoryVO;
import com.homvee.youhui.common.vos.Msg;
import com.homvee.youhui.common.vos.Pager;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Copyright (c) 2018$. ddyunf.com all rights reserved
 *
 * @author Homvee.Tang(tanghongwei @ ddcloudf.com)
 * @version V1.0
 * @Description TODO(用一句话描述该文件做什么)
 * @date 2018-08-17 10:38
 */
@Controller
@RequestMapping(path = "/catg")
public class CategoryCtrl extends BaseCtrl {

   @Resource
   private CategoryService categoryService;

    @RequestMapping(path = {"/add"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
   public Msg save(String catgName , Long parentId){
       if(StringUtils.isEmpty(catgName)){
           return Msg.error("参数错误");
       }
       Category category = new Category();
       category.setCatgName(catgName);
       category.setParentId(parentId);
       category.setUserId(getUser().getId());
       category.setCreator(getUser().getUserName());
       categoryService.save(Lists.newArrayList(category));
       return Msg.success();
   }

    @RequestMapping(path = {"/list"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Msg list(CategoryVO categoryVO, Pager pager){
        categoryVO.setUserId(getUser().getId());
        pager = categoryService.findByConditions(categoryVO , pager);
        return Msg.success(pager);
    }

    @RequestMapping(path = {"/parent"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Msg parent(){
        List<Category> catgs = categoryService.findByParentIdAndUserId(null ,getUser().getId());
        return Msg.success(catgs);
    }

    @RequestMapping(path = {"/children"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Msg children(Long parentId){
        List<Category> catgs = categoryService.findByParentIdAndUserId(parentId , getUser().getId());
        return Msg.success(catgs);
    }

   @RequestMapping(path = {"/one"}, method = {RequestMethod.GET, RequestMethod.POST})
   @ResponseBody
   public Msg findOne(Long id){
       if(StringUtils.isEmpty(id) || id < 1){
           return Msg.error("参数错误");
       }
       Category category = categoryService.findOne(id);
       if(category == null){
           return Msg.error("类目不存在");
       }
        CategoryVO categoryVO = new CategoryVO();
        BeanUtils.copyProperties(category ,categoryVO);
       return Msg.success(categoryVO);
   }

   @RequestMapping(path = {"/edit"}, method = {RequestMethod.GET, RequestMethod.POST})
   @ResponseBody
   public Msg edit(CategoryVO vo){
       if(StringUtils.isEmpty(vo) || vo.getId() < 1){
           return Msg.error("参数错误");
       }
       Category catg = categoryService.findOne(vo.getId());
       if(catg == null){
           return Msg.error("类目不存在");
       }

       BeanUtils.copyProperties(vo ,catg , ArrayUtils.add(BaseVO.getIgnoreProperties() , "userId"));
       catg.setChanger(getUser().getUserName());
       catg.setChangeTime(new Date());
       categoryService.save(Lists.newArrayList(catg));
       return Msg.success();
   }

    @RequestMapping(path = {"/del"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Msg del(Long[] ids){
        if(StringUtils.isEmpty(ids) || ids.length < 1){
            return Msg.error("参数错误");
        }
        categoryService.delByIds(ids);
        return Msg.success();
    }

}
