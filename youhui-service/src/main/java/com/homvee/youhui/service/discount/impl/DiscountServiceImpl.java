package com.homvee.youhui.service.discount.impl;

import com.homvee.youhui.common.vos.DiscountVo;
import com.homvee.youhui.common.vos.Msg;
import com.homvee.youhui.common.vos.Pager;
import com.homvee.youhui.dao.discount.DiscountDao;
import com.homvee.youhui.dao.discount.model.Discount;
import com.homvee.youhui.dao.user.UsrDao;
import com.homvee.youhui.dao.user.model.User;
import com.homvee.youhui.dao.vender.VenderDao;
import com.homvee.youhui.dao.vender.model.Vender;
import com.homvee.youhui.service.discount.DiscountService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("discountService")
public class DiscountServiceImpl implements DiscountService {


    @Autowired
    private DiscountDao discountDao;

    @Autowired
    private VenderDao venderDao;

    @Autowired
    private UsrDao usrDao;


    @Override
    public Msg findByCondition(DiscountVo discountVo) {
        Pager pager = discountDao.findByCondtion(discountVo);
        return Msg.success(pager);
    }

    @Override
    public Msg typeList() {
        List<Map<String,Object>> resultMap = new ArrayList<>();
        List<Object[]> typeList = discountDao.findTypeList();
        if(CollectionUtils.isNotEmpty(typeList)){
            for (int i=0;i<typeList.size();i++){
                Map<String,Object> map = new HashMap<>();
                map.put("id",typeList.get(i)[0]);
                map.put("name",typeList.get(i)[1]);
                resultMap.add(map);
            }
        }
        return Msg.success("获取类型列表成功",resultMap);
    }

    @Override
    public Msg detail(Long id, String openId) {
        Map<String,Object> resultMap = new HashMap<>();
        Discount discount = discountDao.findById(id).get();
        //d.id,d.dname,d.discountType,d.remark,d.detail,v.vname,v.mobile,v.addr,v.logoUrl
        resultMap.put("id",discount.getId());
        resultMap.put("dname",discount.getDname());
        resultMap.put("discountType",discount.getDiscountType());
        resultMap.put("remark",discount.getRemark());
        resultMap.put("detail",discount.getDetail());

        Vender vender = venderDao.findById(discount.getVenderId()).get();
        resultMap.put("vname",vender.getVname());
        resultMap.put("mobile",vender.getMobile());
        resultMap.put("addr",vender.getAddr());
        resultMap.put("logoUrl",vender.getLogoUrl());

        resultMap.put("activated",false);
        //传递openid
        if(StringUtils.isNoneEmpty(openId)){
            User user = usrDao.findByOpenId(openId);
            if(user!=null && user.getActivated()==1){
                resultMap.put("activated",true);
            }
        }
        return Msg.success(resultMap);
    }
}
