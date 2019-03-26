package com.homvee.youhui.web.interceptors;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

@Aspect
public class LoggerAop {

    private ThreadLocal<Long> startTime = new ThreadLocal<>();
    private ThreadLocal<Long> endTime = new ThreadLocal<>();

    private Logger logger = LoggerFactory.getLogger(LoggerAop.class);

    @Pointcut("execution(* com.homvee.youhui.web.ctrls..*.*(..))")
    public void pointcut(){

    }

    @Before("pointcut()")
    public void doBefore(JoinPoint joinPoint){
        startTime.set(System.currentTimeMillis());
        // 接收到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        // 记录下请求内容
        logger.info("请求URL--->{},请求方式--->{},请求IP--->{},请求方法--->{},请求参数---{}",request.getRequestURL().toString(),
                request.getMethod()
                ,request.getRemoteAddr(),
                joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName()
                ,Arrays.toString(joinPoint.getArgs()));
    }

    @AfterReturning(returning = "ret", pointcut = "pointcut()")
    public void doAfterReturning(Object ret) throws Throwable {
        // 处理完请求，返回内容
        endTime.set(System.currentTimeMillis());
        logger.info("返回内容--->{},耗时--->{}: ", ret,(endTime.get()-startTime.get())+"豪秒");
    }


}
