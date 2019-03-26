package com.homvee.youhui.web.interceptors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    private Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Map<String,Object> jsonErrorHandler(HttpServletRequest req, Exception e) throws Exception {
        logger.error("异常--->{}",e);
        Map<String,Object> r = new HashMap<>();
        r.put("error","系统出错,稍后再试");
        return r;
    }
}
