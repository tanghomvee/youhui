package com.homvee.youhui.web.ctrls;


import com.homvee.youhui.common.vos.DiscountVo;
import com.homvee.youhui.common.vos.Msg;
import com.homvee.youhui.dao.user.model.User;
import com.homvee.youhui.service.discount.DiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/youhui/discount")
public class DiscountCtr {

    @Autowired
    private DiscountService discountService;

    /**
     * 优惠卷列表
     */
    @RequestMapping(path = {"/list"} )
    @ResponseBody
    public Msg list(DiscountVo discountVo){
        Msg msg = discountService.findByCondition(discountVo);
        return msg;
    }

    /**
     * 优惠卷列表
     */
    @RequestMapping(path = {"/detail"} )
    @ResponseBody
    public Msg detail(Long id,String openId){
        Msg msg = discountService.detail(id,openId);
        return msg;
    }
}
