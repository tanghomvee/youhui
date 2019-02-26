package com.homvee.youhui.web.ctrls;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.homvee.youhui.common.components.RedisComponent;
import com.homvee.youhui.common.enums.WayEnum;
import com.homvee.youhui.common.enums.YNEnum;
import com.homvee.youhui.common.vos.BaseVO;
import com.homvee.youhui.common.vos.ContentVO;
import com.homvee.youhui.common.vos.Msg;
import com.homvee.youhui.common.vos.Pager;
import com.homvee.youhui.dao.acct.model.Account;
import com.homvee.youhui.dao.content.model.Content;
import com.homvee.youhui.dao.room.model.Room;
import com.homvee.youhui.dao.user.model.User;
import com.homvee.youhui.service.acct.AccountService;
import com.homvee.youhui.service.content.ContentService;
import com.homvee.youhui.service.room.RoomService;
import com.homvee.youhui.service.user.UserService;
import com.homvee.youhui.web.BaseCtrl;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Copyright (c) 2018$. ddyunf.com all rights reserved
 *
 * @author Homvee.Tang(tanghongwei @ ddcloudf.com)
 * @version V1.0
 * @Description TODO(用一句话描述该文件做什么)
 * @date 2018-08-17 10:38
 */
@Controller
@RequestMapping(path = "/content")
public class ContentCtrl extends BaseCtrl {

   @Resource
   private ContentService contentService;

    @Resource
    private RoomService roomService;

    @Resource
    private AccountService accountService;

    @Resource
    private UserService userService;

    @Resource
    private RedisComponent redisComponent;



   @RequestMapping(path = {"/add"}, method = {RequestMethod.GET, RequestMethod.POST})
   @ResponseBody
   public Msg save(ContentVO contentVO){
       
       if(StringUtils.isEmpty(contentVO.getCatgId()) || contentVO.getCatgId() < 1
               || StringUtils.isEmpty(contentVO.getAcctId()) || contentVO.getAcctId() < 1
               || StringUtils.isEmpty(contentVO.getRoomId()) || contentVO.getRoomId() < 1){
           return Msg.error("参数错误");
       }

       Content content = new Content();
       BeanUtils.copyProperties(contentVO ,content);
       content.setUsed(0);
       content.setUserId(getUser().getId());
       content.setCreator(getUser().getUserName());
       content.setYn(YNEnum.YES.getVal());
       contentService.save(Lists.newArrayList(content));
       return Msg.success();
   }
   @RequestMapping(path = {"/list"}, method = {RequestMethod.GET, RequestMethod.POST})
   @ResponseBody
   public Msg list(ContentVO contentVO, Pager pager){
       contentVO.setUserId(getUser().getId());
       pager = contentService.findByConditions(contentVO , pager);
       return Msg.success(pager);
   }

   @RequestMapping(path = {"/one"}, method = {RequestMethod.GET, RequestMethod.POST})
   @ResponseBody
   public Msg findOne(Long id){
       if(StringUtils.isEmpty(id) || id < 1){
           return Msg.error("参数错误");
       }
       Content content = contentService.findOne(id);
       if(content == null){
           return Msg.error("直播内容不存在");
       }
       ContentVO contentVO = new ContentVO();
       BeanUtils.copyProperties(content ,contentVO);
       return Msg.success(contentVO);
   }
   @RequestMapping(path = {"/del"}, method = {RequestMethod.GET, RequestMethod.POST})
   @ResponseBody
   public Msg del(Long[] ids){
       if(StringUtils.isEmpty(ids) || ids.length < 1){
           return Msg.error("参数错误");
       }
       contentService.delByIds(ids);
       return Msg.success();
   }

   @RequestMapping(path = {"/edit"}, method = {RequestMethod.GET, RequestMethod.POST})
   @ResponseBody
   public Msg edit(ContentVO contentVO){
       if(StringUtils.isEmpty(contentVO) || contentVO.getId() < 1){
           return Msg.error("参数错误");
       }
       Content content = contentService.findOne(contentVO.getId());
       if(content == null){
           return Msg.error("直播内容不存在");
       }

       BeanUtils.copyProperties(contentVO ,content , ArrayUtils.add(BaseVO.getIgnoreProperties() , "userId"));
       content.setChanger(getUser().getUserName());
       content.setChangeTime(new Date());
       contentService.save(Lists.newArrayList(content));
       return Msg.success();
   }



    /**
     * {
     * 	"content":"发言内容或者URL",
     * 	"operate":"操作类型"//chat:发言标志;wait:等待发言;go:跳转到指定的房间此时content的数据为URL
     * }
     * @param url
     * @param acctName
     * @param authKey
     */

   @RequestMapping(path = {"/chat"}, method = {RequestMethod.GET, RequestMethod.POST})
   @ResponseBody
   public  Msg chat(String url , String acctName , String authKey){


       if(StringUtils.isEmpty(url) || StringUtils.isEmpty(acctName) || StringUtils.isEmpty(authKey)){
           return Msg.error("参数错误");
       }
       acctName = acctName.trim();

       User user = userService.findByAuthKey(authKey);

       if(user == null){
           return Msg.error("非法账户");
       }

       List<Account> accounts = accountService.findByAcctNameAndUserId(acctName , user.getId());
       if(CollectionUtils.isEmpty(accounts) || accounts.size() != 1){
           return Msg.error("账户不存在");
       }
       Account account = accounts.get(0);
       JSONObject retJSON = new JSONObject();
       String operate = "wait" ;
       Object txt = 5000;

       List<Room> rooms = roomService.findByUrlAndUserId(url , user.getId());
       if(CollectionUtils.isEmpty(rooms) || rooms.size() != 1){
           rooms = roomService.findByAcctIdAndUserId(account.getId() , user.getId());
           if (!CollectionUtils.isEmpty(rooms)){
               Random random = new Random();
               Room tmpRoom = rooms.get(random.nextInt(rooms.size()));
               retJSON.put("operate" , "go");
               retJSON.put("content" , tmpRoom.getUrl());
               return Msg.success(retJSON);
           }


           return Msg.error("直播间未配置");
       }

       Room room = rooms.get(0);
       if (WayEnum.STOP.getVal().equals(room.getWay())){
           retJSON.put("operate" , operate);
           retJSON.put("content" , txt);
           return Msg.success(retJSON);
       }


       Long interval = room.getIntervalTime();

       if(interval != null ){
           if (!redisComponent.setStrNx(room.getId().toString(), interval) ){
               retJSON.put("operate" , operate);
               retJSON.put("content" , interval * 1000);
               return Msg.success(retJSON);
           }
       }

       Content content = null;
       if (WayEnum.AUTO.getVal().equals(room.getWay())){
           content = contentService.autoContent(room.getId() , user.getId(), account);
           content.setContent(content.getContent());
           if (content == null){
               retJSON.put("operate" , operate);
               retJSON.put("content" , 2000);
               return Msg.success(retJSON);
           }
       }else {
           if(WayEnum.NORMAL.getVal().equals(room.getWay())){
               content = contentService.nextContent(room.getId() , user.getId() , account.getId());
           }else{
               content = contentService.loopContent(room.getId() , user.getId() , account.getId());
           }
       }

       if (content != null){
           operate = "chat";
           txt = content.getContent();
       }

       retJSON.put("operate" , operate);
       retJSON.put("content" , txt);
       return Msg.success(retJSON);
   }


}
