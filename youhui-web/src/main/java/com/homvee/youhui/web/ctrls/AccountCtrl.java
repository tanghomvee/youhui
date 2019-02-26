package com.homvee.youhui.web.ctrls;

import com.google.common.collect.Lists;
import com.homvee.youhui.dao.acct.model.Account;
import com.homvee.youhui.service.acct.AccountService;
import com.homvee.youhui.web.BaseCtrl;
import com.homvee.youhui.common.vos.AccountVO;
import com.homvee.youhui.common.vos.BaseVO;
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
@RequestMapping(path = "/acct")
public class AccountCtrl extends BaseCtrl {

   @Resource
   private AccountService accountService;

    @RequestMapping(path = {"/add"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
   public Msg save(AccountVO accountVO){
       if(StringUtils.isEmpty(accountVO.getAcctName()) || StringUtils.isEmpty(accountVO.getMobile())){
           return Msg.error("参数错误");
       }
       Account account = new Account();
       BeanUtils.copyProperties(accountVO ,account ,BaseVO.getIgnoreProperties());
       account.setUserId(getUser().getId());
       account.setCreator(getUser().getUserName());
       account.setAcctName(accountVO.getAcctName().trim());
       accountService.save(Lists.newArrayList(account));
       return Msg.success();
   }

    @RequestMapping(path = {"/list"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Msg list(AccountVO accountVO, Pager pager){
        accountVO.setUserId(getUser().getId());
        pager = accountService.findByConditions(accountVO , pager);
        return Msg.success(pager);
    }

    @RequestMapping(path = {"/all"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Msg all(){
        List<Account> accounts = accountService.findByUserId(getUser().getId());
        return Msg.success(accounts);
    }

   @RequestMapping(path = {"/one"}, method = {RequestMethod.GET, RequestMethod.POST})
   @ResponseBody
   public Msg findOne(Long id){
       if(StringUtils.isEmpty(id) || id < 1){
           return Msg.error("参数错误");
       }
       Account account = accountService.findOne(id);
       if(account == null){
           return Msg.error("账户不存在");
       }
        AccountVO accountVO = new AccountVO();
        BeanUtils.copyProperties(account ,accountVO);
       return Msg.success(accountVO);
   }
   @RequestMapping(path = {"/del"}, method = {RequestMethod.GET, RequestMethod.POST})
   @ResponseBody
   public Msg del(Long[] ids){
       if(StringUtils.isEmpty(ids) || ids.length < 1){
           return Msg.error("参数错误");
       }
       accountService.delByIds(ids);
       return Msg.success();
   }

   @RequestMapping(path = {"/edit"}, method = {RequestMethod.GET, RequestMethod.POST})
   @ResponseBody
   public Msg edit(AccountVO accountVO){
       if(StringUtils.isEmpty(accountVO) || accountVO.getId() < 1){
           return Msg.error("参数错误");
       }
       Account account = accountService.findOne(accountVO.getId());
       if(account == null){
           return Msg.error("账户不存在");
       }

       BeanUtils.copyProperties(accountVO ,account , ArrayUtils.add(BaseVO.getIgnoreProperties() , "userId"));
       account.setChanger(getUser().getUserName());
       account.setChangeTime(new Date());
       account.setAcctName(account.getAcctName().trim());
       accountService.save(Lists.newArrayList(account));
       return Msg.success();
   }

}
