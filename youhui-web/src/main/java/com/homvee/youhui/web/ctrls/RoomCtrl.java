package com.homvee.youhui.web.ctrls;

import com.google.common.collect.Lists;
import com.homvee.youhui.common.enums.WayEnum;
import com.homvee.youhui.common.vos.BaseVO;
import com.homvee.youhui.common.vos.Msg;
import com.homvee.youhui.common.vos.Pager;
import com.homvee.youhui.common.vos.RoomVO;
import com.homvee.youhui.dao.room.model.Room;
import com.homvee.youhui.service.room.RoomService;
import com.homvee.youhui.web.BaseCtrl;
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
@RequestMapping(path = "/room")
public class RoomCtrl extends BaseCtrl {

   @Resource
   private RoomService roomService;

   @RequestMapping(path = {"/add"}, method = {RequestMethod.GET, RequestMethod.POST})
   @ResponseBody
   public Msg save(String roomName ,String mobile ,Integer way , String url,Long intervalTime ,Integer startHour ,Integer endHour , String defaultContent){
       if(StringUtils.isEmpty(roomName) || StringUtils.isEmpty(url)|| StringUtils.isEmpty(mobile)){
           return Msg.error("参数错误");
       }
       Room room = new Room();
       room.setRoomName(roomName);
       room.setUrl(url);
       if(WayEnum.getByVal(way) != null){
           room.setWay(way);
       }else {
           room.setWay(WayEnum.NORMAL.getVal());
       }
       room.setUserId(getUser().getId());
       room.setCreator(getUser().getUserName());
       room.setIntervalTime(intervalTime);
       room.setEndHour(endHour == null || endHour < 0 ? 0 : endHour);
       room.setStartHour(startHour == null || startHour < 0 ? 0 : startHour);
       room.setMobile(mobile);
       room.setDefaultContent(defaultContent);
       roomService.save(Lists.newArrayList(room));
       return Msg.success();
   }
   @RequestMapping(path = {"/list"}, method = {RequestMethod.GET, RequestMethod.POST})
   @ResponseBody
   public Msg list(RoomVO roomVO, Pager pager){
       roomVO.setUserId(getUser().getId());
       pager = roomService.findByConditions(roomVO , pager);
       return Msg.success(pager);
   }
   @RequestMapping(path = {"/all"}, method = {RequestMethod.GET, RequestMethod.POST})
   @ResponseBody
   public Msg all(){
       List<Room>  rooms= roomService.findByUserId(getUser().getId());
       return Msg.success(rooms);
   }

   @RequestMapping(path = {"/one"}, method = {RequestMethod.GET, RequestMethod.POST})
   @ResponseBody
   public Msg findOne(Long id){
       if(StringUtils.isEmpty(id) || id < 1){
           return Msg.error("参数错误");
       }
       Room room = roomService.findOne(id);
       if(room == null){
           return Msg.error("直播间不存在");
       }
        RoomVO roomVO = new RoomVO();
        BeanUtils.copyProperties(room ,roomVO);
       return Msg.success(roomVO);
   }

   @RequestMapping(path = {"/edit"}, method = {RequestMethod.GET, RequestMethod.POST})
   @ResponseBody
   public Msg edit(RoomVO roomVO){
       if(StringUtils.isEmpty(roomVO.getRoomName()) || StringUtils.isEmpty(roomVO.getUrl()) || StringUtils.isEmpty(roomVO.getMobile()) || roomVO.getId() < 1){
           return Msg.error("参数错误");
       }
       Room room = roomService.findOne(roomVO.getId());
       if(room == null){
           return Msg.error("直播间不存在");
       }

       BeanUtils.copyProperties(roomVO ,room , ArrayUtils.add(BaseVO.getIgnoreProperties() , "userId"));
       room.setChanger(getUser().getUserName());
       room.setChangeTime(new Date());
       roomService.save(Lists.newArrayList(room));

       return Msg.success();
   }
   @RequestMapping(path = {"/switch"}, method = {RequestMethod.GET, RequestMethod.POST})
   @ResponseBody
   public Msg switchWay(Long id , Integer way){
       if(StringUtils.isEmpty(id) || id < 1 || way == null || WayEnum.getByVal(way) == null){
           return Msg.error("参数错误");
       }
       Room room = roomService.findOne(id);
       if(room == null){
           return Msg.error("直播间不存在");
       }
       room.setWay(way);
       room.setChanger(getUser().getUserName());
       room.setChangeTime(new Date());
       roomService.save(Lists.newArrayList(room));
       return Msg.success();
   }

    @RequestMapping(path = {"/del"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Msg del(Long[] ids){
        if(StringUtils.isEmpty(ids) || ids.length < 1){
            return Msg.error("参数错误");
        }
        roomService.delByIds(ids);
        return Msg.success();
    }

}
