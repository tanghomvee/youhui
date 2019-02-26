package com.homvee.youhui.web.ctrls;

import com.google.common.collect.Lists;
import com.homvee.youhui.common.enums.EncryptionEnum;
import com.homvee.youhui.common.vos.BaseVO;
import com.homvee.youhui.common.vos.Msg;
import com.homvee.youhui.common.vos.UserVO;
import com.homvee.youhui.dao.user.model.User;
import com.homvee.youhui.service.user.UserService;
import com.homvee.youhui.web.BaseCtrl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * Copyright (c) 2018$. ddyunf.com all rights reserved
 *
 * @author Homvee.Tang(tanghongwei @ ddcloudf.com)
 * @version V1.0
 * @Description TODO(用一句话描述该文件做什么)
 * @date 2018-08-17 10:38
 */
@Controller
@RequestMapping(path = "/user")
public class UserCtrl extends BaseCtrl {

   @Resource
   private UserService userService;

   @RequestMapping(path = {"/login"}, method = {RequestMethod.GET, RequestMethod.POST})
   @ResponseBody
   public Msg login(String mobile , String chkCode ){

       if(StringUtils.isEmpty(mobile) || StringUtils.isEmpty(chkCode)){
           return Msg.error("参数错误");
       }
       try {
           chkCode = EncryptionEnum.RSA.decode(chkCode);
           chkCode = EncryptionEnum.MD5_2_BASE64.encrypt(chkCode);
       } catch (Exception e) {
           LOGGER.error("解密错误:pwd={}" , chkCode ,e);
           return Msg.error("密码错误");
       }

       User user = userService.findByUserNameAndPwd(mobile , chkCode);
       if(user == null){
           return Msg.error("账户不存在");
       }
       UserVO userVO = new UserVO();
       BeanUtils.copyProperties(user ,userVO , BaseVO.getIgnoreProperties());
       userVO.setPwd(null);
       setUser(userVO);
       return Msg.success(userVO);
   }
   @RequestMapping(path = {"/setting"}, method = {RequestMethod.GET, RequestMethod.POST})
   @ResponseBody
   public Msg setting(String userName ,String oldPwd, String newPwd , String reNewPwd , String mobile){

       if(StringUtils.isEmpty(userName) ||StringUtils.isEmpty(oldPwd) || StringUtils.isEmpty(newPwd) || StringUtils.isEmpty(reNewPwd)|| StringUtils.isEmpty(mobile)){
           return Msg.error("参数错误");
       }

       String pwd = null;
       try {
           newPwd = EncryptionEnum.RSA.decode(newPwd);
           reNewPwd = EncryptionEnum.RSA.decode(reNewPwd);
           if (!newPwd.equals(reNewPwd)){
               return Msg.error("两次输入的密码不一致");
           }
           newPwd = EncryptionEnum.MD5_2_BASE64.encrypt(newPwd);

           pwd = EncryptionEnum.RSA.decode(oldPwd);
           pwd = EncryptionEnum.MD5_2_BASE64.encrypt(pwd);
       } catch (Exception e) {
           LOGGER.error("解密错误:pwd={}" , pwd ,e);
           return Msg.error("密码错误");
       }



       User user = userService.findByUserNameAndPwd(userName , pwd);
       if(user == null){
           return Msg.error("账户不存在");
       }
       user.setPwd(newPwd);
       user.setMobile(mobile);
       userService.save(Lists.newArrayList(user));

       UserVO userVO = new UserVO();
       BeanUtils.copyProperties(user ,userVO , BaseVO.getIgnoreProperties());
       userVO.setPwd(null);
       setUser(userVO);
       return Msg.success(userVO);
   }

}
