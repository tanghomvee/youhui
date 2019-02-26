package com.homvee.youhui.task.impl;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.homvee.youhui.common.enums.WayEnum;
import com.homvee.youhui.dao.room.model.Room;
import com.homvee.youhui.dao.sms.model.PortInfo;
import com.homvee.youhui.dao.sms.model.SendingSMS;
import com.homvee.youhui.dao.user.model.User;
import com.homvee.youhui.service.room.RoomService;
import com.homvee.youhui.service.sms.PortInfoService;
import com.homvee.youhui.service.sms.SendingSMSService;
import com.homvee.youhui.service.user.UserService;
import com.homvee.youhui.task.LiveBroadcastTask;
import org.joda.time.DateTime;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

/**
 * Copyright (c) 2018$. ddyunf.com all rights reserved
 *
 * @author Homvee.Tang(tanghongwei @ ddcloudf.com)
 * @version V1.0
 * @Description TODO(用一句话描述该文件做什么)
 * @date 2018-09-25 13:23
 */


/**
 * Copyright (c) 2018$. ddyunf.com all rights reserved
 *
 * @author Homvee.Tang(tanghongwei@ddcloudf.com)
 * @version V1.0
 * @Description TODO(用一句话描述该文件做什么)
 * @date 2018-05-21 10:05
 */
@Component("roomOnlineCheckTask")
public class RoomOnlineCheckTask implements LiveBroadcastTask {

    protected Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Resource
    private RoomService roomService;

    @Resource
    private SendingSMSService sendingSMSService;

    @Resource
    private PortInfoService portInfoService;

    @Resource
    private UserService userService;

    @Override
    public void execute() {


        List<Room> rooms = roomService.findByWay(WayEnum.NORMAL.getVal() , WayEnum.AUTO.getVal() , WayEnum.LOOP.getVal());
        if (CollectionUtils.isEmpty(rooms)){
            return;
        }

        Integer now = DateTime.now().getHourOfDay();
        for (Room room : rooms){
            Integer start = room.getStartHour();
            if(start == null || start < 0){
                start = 0 ;
            }
            if (now < start){
               continue;
            }
            Integer end = room.getEndHour();

            if (end == null || end < 0){
                end = 23;
            }


            if (now > end){
              continue;
            }

            if (StringUtils.isEmpty(room.getUrl()) || isOnline(room.getUrl())){
                continue;
            }

            String mobile = room.getMobile();
            String content = room.getRoomName() + " 同学,你的粉丝望眼欲穿的期待你上线 加油 ^_^";

            try{
                PortInfo portInfo = portInfoService.findRandPhoneNum();
                SendingSMS sendingSMS4Room = new SendingSMS();
                sendingSMS4Room.setPhoNum(portInfo.getPhoNum());
                sendingSMS4Room.setPortNum(portInfo.getPortNum());
                sendingSMS4Room.setSmsContent(content);
                sendingSMS4Room.setSmsNumber(mobile);
                sendingSMS4Room.setSmsType(0);
                sendingSMS4Room = sendingSMSService.save(sendingSMS4Room, 60*60L);
                if (sendingSMS4Room.getId() != null){
                    LOGGER.info("记录主播未直播的短信记录:room={} , sms={}" , room.getId() , content);
                }

                SendingSMS sendingSMS4User = new SendingSMS();
                User user = userService.findOne(room.getUserId());
                mobile = user.getMobile();
                content = "催促  " + room.getRoomName() + " 直播";
                sendingSMS4User.setPhoNum(portInfo.getPhoNum());
                sendingSMS4User.setPortNum(portInfo.getPortNum());
                sendingSMS4User.setSmsContent(content);
                sendingSMS4User.setSmsNumber(mobile);
                sendingSMS4User.setSmsType(0);
                sendingSMS4User = sendingSMSService.save(sendingSMS4User, 60*60L);
                if (sendingSMS4User.getId() != null){
                    LOGGER.info("记录主播未直播的短信记录:room={} , sms={}" , room.getId() , content);
                }
            }catch (Exception ex){
                LOGGER.error("记录主播未直播的短信记录异常:room={} " ,  room.getId(), ex);
            }

        }

    }

    private boolean isOnline(String url){
        WebClient webClient = buildWebClient();
        //获取网页
        HtmlPage htmlPage = null;
        try {
            htmlPage = webClient.getPage(url);
            Document doc = Jsoup.parse(htmlPage.asXml());
            Elements timetitElt = doc.getElementsByAttributeValue("data-anchor-info" , "timetit");
            if (timetitElt.isEmpty()){
                return true;
            }
            return  !timetitElt.get(0).hasText();
        } catch (IOException e) {
            LOGGER.error("获取网页异常:url={}" , url , e);
        }finally {
            webClient.close();
        }


        return false;
    }

    public WebClient buildWebClient(){
        // 1创建WebClient
        WebClient webClient=new WebClient(BrowserVersion.CHROME);
        // 2 启动JS
        webClient.getOptions().setJavaScriptEnabled(true);
        // 3 禁用Css，可避免自动二次請求CSS进行渲染
        webClient.getOptions().setCssEnabled(false);
        // 4 启动客戶端重定向
        webClient.getOptions().setRedirectEnabled(true);
        // 5 js运行错誤時，是否拋出异常
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        // 6 设置超时
        webClient.getOptions().setTimeout(50000);
        //支持https
        webClient.getOptions().setUseInsecureSSL(true);

        webClient.getOptions().setDoNotTrackEnabled(false);
        //设置js运行超时时间
        webClient.setJavaScriptTimeout(8000);
        //设置页面等待js响应时间，等待JS驱动dom完成获得还原后的网页
        webClient.waitForBackgroundJavaScript(10000);
        return webClient;
    }
}

