package com.homvee.youhui.service.content.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.homvee.youhui.common.components.RedisComponent;
import com.homvee.youhui.common.constants.RedisKey;
import com.homvee.youhui.common.enums.SeparatorEnum;
import com.homvee.youhui.common.enums.YNEnum;
import com.homvee.youhui.common.vos.ContentVO;
import com.homvee.youhui.common.vos.Pager;
import com.homvee.youhui.dao.acct.model.Account;
import com.homvee.youhui.dao.catg.model.Category;
import com.homvee.youhui.dao.content.ContentDao;
import com.homvee.youhui.dao.content.model.Content;
import com.homvee.youhui.dao.room.model.Room;
import com.homvee.youhui.service.BaseServiceImpl;
import com.homvee.youhui.service.catg.CategoryService;
import com.homvee.youhui.service.content.ContentService;
import com.homvee.youhui.service.room.RoomService;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.*;

/**
 * Copyright (c) 2018$. ddyunf.com all rights reserved
 *
 * @author Homvee.Tang(tanghongwei @ ddcloudf.com)
 * @version V1.0
 * @Description TODO(用一句话描述该文件做什么)
 * @date 2018-08-17 14:40
 */
@Service("contentService")
public class ContentServiceImpl extends BaseServiceImpl<Content , Long> implements ContentService {

    @Resource
    private ContentDao contentDao;
    @Resource
    private CategoryService categoryService;
    @Resource
    private RoomService roomService;
    @Resource
    private RedisComponent redisComponent;

    private String[] randStrs = new String[]{
//                "^_^ "," ԅ(¯㉨¯ԅ) "," （￢㉨￢）   ","  ٩(♡㉨♡ )۶  ","  ヽ(○^㉨^)ﾉ♪ ","  (╥ ㉨ ╥`)   ","  ҉٩(*^㉨^*)  ",
//                " （≧㉨≦） "," （⊙㉨⊙） "," (๑•́ ㉨ •̀๑) "," ◟(░´㉨`░)◜ ",
//            "·","^","`",".","_","~",",","、","¯","♡","o_o","I","i","|","l"
//            "来","哈哈","呀！", "呢","好","哦","吗？","666666","go","888888","9494","关注主播,惊喜连连","送点什么","闹热","火火","🐉","🐮","㊣","哇","嘛","高","行","各位斗友,走一波打赏"
            "强","厉害","来","哈哈","呀！", "呢","好","哦","吗？","666666","go","888888","9494","关注主播,惊喜连连","送点什么","闹热","火火","哇","嘛","高","行","各位斗友,走一波打赏"
    };
    private String[] emots = new String[]{
            "[emot:dy101]", "[emot:dy102]", "[emot:dy103]", "[emot:dy104]", "[emot:dy105]", "[emot:dy106]", "[emot:dy107]", "[emot:dy108]", "[emot:dy109]",
            "[emot:dy110]", "[emot:dy111]", "[emot:dy112]", "[emot:dy113]", "[emot:dy114]", "[emot:dy115]", "[emot:dy116]", "[emot:dy117]", "[emot:dy118]",
            "[emot:dy119]", "[emot:dy120]", "[emot:dy121]", "[emot:dy122]", "[emot:dy123]", "[emot:dy124]", "[emot:dy125]", "[emot:dy126]", "[emot:dy127]",
            "[emot:dy128]", "[emot:dy129]", "[emot:dy130]", "[emot:dy131]", "[emot:dy132]", "[emot:dy133]", "[emot:dy134]", "[emot:dy135]", "[emot:dy136]",
            "[emot:dy137]", "[emot:dy001]", "[emot:dy002]", "[emot:dy003]", "[emot:dy004]", "[emot:dy005]", "[emot:dy006]", "[emot:dy007]", "[emot:dy008]",
            "[emot:dy009]", "[emot:dy010]", "[emot:dy011]", "[emot:dy012]", "[emot:dy013]", "[emot:dy014]", "[emot:dy015]", "[emot:dy016]", "[emot:dy017]"
    };

    private String[] separators =  new String[]{"㊣","🐉","💗","🐮","❀","👑","🌹","👍","👌","✏","🦐","🧜‍","🐱","🚩","🎸","🖖","👄","🐰","🎈","🎉","🎁","🔥","☀","🎵","🎶","🎼","🔫"};
    private String[] punctuations =  new String[]{".","..","...","!","!!","^_^","♡",".。","o_o","*","㉨","⊙","🎤","🎙","҉","……"};

    @Override
    public List<Content> save(List<Content> contents) {
        return contentDao.saveAll(contents);
    }

    @Override
    public Content findOne(Long id) {
        Optional<Content> optional = contentDao.findById(id);
        return optional != null && optional.isPresent() ? optional.get() : null;
    }

    @Override
    public Pager findByConditions(ContentVO contentVO, Pager pager) {
        pager = contentDao.findByConditions(contentVO , pager);
        if(pager != null && !CollectionUtils.isEmpty(pager.getData())){

            List<ContentVO> vos = Lists.newArrayList();
            for (Object obj: pager.getData() ){
                String tmp = JSONObject.toJSONString(obj);
                ContentVO vo = JSON.toJavaObject(JSONObject.parseObject(tmp) , ContentVO.class);
                vos.add(vo);

                Category catg = categoryService.findOne(vo.getCatgId());

                if(catg != null){
                    vo.setCatgName(catg.getCatgName());
                    vo.setParentCatgId(catg.getId());
                    if(catg.getParentId() != null){
                        vo.setParentCatgId(catg.getParentId());
                    }
                }


                if(vo.getPreId() != null){
                    Content preContent = this.findOne(vo.getPreId());
                    if(preContent != null){
                        vo.setPreContent(preContent.getContent());
                    }
                }

            }
            pager.setData(vos);
        }
        return pager;
    }



    /**
     * 如果访问量大需要优化
     * @param roomId
     * @param userId
     * @param account
     * @return
     */
    @Override
    public Content autoContent(Long roomId, Long userId, Account account) {
        List<Content> contents = contentDao.findByRoomIdAndAcctIdAndUserId(roomId,account.getId() , userId);
        if(CollectionUtils.isEmpty(contents)){
            return null;
        }

        Content content = contents.get(0);

        String acctKey = RedisKey.ACCOUNT + SeparatorEnum.MIDDLE_LINE.getVal() + account.getId();
        Long fiveMinutes = 300L;
        Long preFiveMinutes = System.currentTimeMillis() - fiveMinutes*1000;
        //移除5分钟未发言的房间
        redisComponent.removeZSetValByScore(acctKey , 0L , preFiveMinutes );

        //当前账号所在的房间列表
        LinkedHashSet<String> vals = redisComponent.getZSetVal(acctKey , 0L ,Long.valueOf(Integer.MAX_VALUE));
        //如果房间列表为空,将此当前房间ID放入有序队列,返回空，让前端等待,以防同时在多个房间发言
        if(CollectionUtils.isEmpty(vals)){
            redisComponent.addZSet(acctKey , roomId.toString() , System.currentTimeMillis());
            LOGGER.info("账号发言房间队列为空:acct={}", account.getAcctName());
            return null;
        }
        Room room = roomService.findOne(roomId);
        //判断当前是否该此房间发言
        if (Lists.newArrayList(vals).indexOf(roomId.toString()) == -1){
            redisComponent.addZSet(acctKey , roomId.toString() , System.currentTimeMillis());
            LOGGER.info("账号发言房间不存在有序队列之中:acct={},room={}", account.getAcctName() , room.getRoomName());
            return null;
        }
        if (Lists.newArrayList(vals).indexOf(roomId.toString()) > 0){
            LOGGER.info("此刻账号不应该在此房间发言:acct={},room={}", account.getAcctName() , room.getRoomName() );
            return null;
        }
        /**
         * 每个账号发言间隔5秒
         */
        Long period = 5L;
        if (account.getPeriod() != null && account.getPeriod() > 0){
            period = account.getPeriod().longValue();
        }
        if(!redisComponent.setStrNx(RedisKey.ACCOUNT + SeparatorEnum.UNDERLINE.getVal() + account.getId() , period)){
            LOGGER.info("账号发言时间未到:acct={}", account.getAcctName());
            return null;
        }

        //用当前时间作为当前房间的权重
        redisComponent.addZSet(acctKey , roomId.toString() , System.currentTimeMillis());
        //计数当前账号在此房间发言的次数
        Long count = redisComponent.incr(acctKey + SeparatorEnum.MIDDLE_LINE.getVal() + roomId , fiveMinutes);
        LOGGER.info("账号已在房间发言次数:acct={},count={}", account.getAcctName(),  count);
        String txt = count != null && count % 50 == 0 ? content.getContent() : "";
        //判断账号在此房间30分钟之内不能发送相同的弹幕
        String contentKey = "";
        if (!StringUtils.isEmpty(txt)){
            contentKey = acctKey + SeparatorEnum.MIDDLE_LINE.getVal() + roomId + SeparatorEnum.UNDERLINE.getVal() + txt;
            if (!redisComponent.setStrNx(contentKey , fiveMinutes * 6)){
                txt = null;
            }
        }
        //循环获取账号在当前房间的弹幕
        while (StringUtils.isEmpty(txt)){
            Random random = new Random();
            int num = random.nextInt(6);
            if (num < 3){
                num = 3;
            }
            txt = getRandomStr(num , room.getDefaultContent());
            Integer maxCnt = 50;
            if (txt.length() < maxCnt){
                if (count != null && count % 20 ==0){
                    int cnt = (maxCnt - txt.length()) / emots[0].length();
                    if (cnt > 0){
                        txt = txt + getRandomEmotion(cnt);
                    }
                }
            }
            contentKey = acctKey + SeparatorEnum.MIDDLE_LINE.getVal() + roomId + SeparatorEnum.UNDERLINE.getVal() + txt;
            if (!redisComponent.setStrNx(contentKey , fiveMinutes * 6)){
                txt = null;
            }
        }

        content.setContent(txt);
        return content;
    }


    @Override
    public  Content nextContent(Long roomId, Long userId, Long accountId) {
        Content content = contentDao.findByRoomIdAndUserIdAndAndNotUsed(roomId,userId,accountId);
        if(content == null){
            return null;
        }
        Long preId = content.getPreId();
        if(preId != null && preId > 0){
            Content contentTmp = this.findOne(preId);
            if(contentTmp != null && contentTmp.getUsed() == 0){
                return null;
            }
        }
        content.setRecentUsedTime(new Date());
        content.setUsed(1);
        contentDao.save(content);
        return content;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delByIds(Long[] ids) {
        for (Long id : ids){
            contentDao.deleteById(id);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public  Content loopContent(Long roomId, Long userId, Long acctId) {
        Integer cntUsed = contentDao.countUsedByRoomId(roomId);
        Integer cnt = contentDao.countByRoomId(roomId);
        if (cnt.equals(cntUsed)){
            contentDao.resetUsed(roomId);
        }
        Content retContent = null;
        Content content = nextContent(roomId,  userId, acctId);
        if (content != null){
            retContent = new Content();
            retContent.setContent(content.getContent() + getRandomStr(2 , null) + getRandomEmotion(2));
        }
        return retContent;
    }

    @Override
    public List<BigInteger> findAcctByRoomId(Long roomId) {
        return contentDao.findAcctByRoomId(roomId);
    }

    @Override
    public List<Content> findAll() {
        return contentDao.findByYnOrderByRoomId(YNEnum.YES.getVal());
    }

    @Override
    public List<Content> findByRoomId(Long roomId) {
        return contentDao.findByRoomIdAndYn(roomId ,YNEnum.YES.getVal());
    }

    @Override
    public List<Content> findByAcctIdAndRoomId(Long acctId, Long roomId) {
        return contentDao.findByAcctIdAndRoomIdAndYn(acctId , roomId , YNEnum.YES.getVal());
    }

    private String getRandomStr(int num , String defaultContent){
        String[] data = randStrs;
        if (!StringUtils.isEmpty(defaultContent)){
            String[] customerData = defaultContent.split(SeparatorEnum.COMMA.getVal());
            if (!ArrayUtils.isEmpty(customerData)){
                data = ArrayUtils.addAll(data , customerData);
            }
        }
        num = data.length > num ? num : data.length;
        String rs = SeparatorEnum.COMMA.getVal();
        Set<String> contents = Sets.newHashSet();
        Random random = new Random();
        while (num > 0){

            String content = data[random.nextInt(data.length)];
            if (contents.contains(content)){
                continue;
            }
            num--;
            contents.add(content);
            String tmpResult = rs + content + separators[random.nextInt(separators.length)];
            if (tmpResult.length() >= 50){
                return  tmpResult;
            }
            rs = tmpResult;
        }

        return rs + punctuations[random.nextInt(punctuations.length)];
    }
    private String getRandomEmotion(int num){
        String rs = "";
        Random random = new Random();
        Set<String> emotsTmp = Sets.newHashSet();
        while (num > 0){
            String emot = emots[random.nextInt(emots.length)];
            if (emotsTmp.contains(emot)){
                continue;
            }
            emotsTmp.add(emot);
            rs = rs + emot;
            num--;
        }

        return rs;
    }

}
