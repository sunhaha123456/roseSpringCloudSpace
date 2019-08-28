package com.rose.aspect;

import com.rose.common.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.BindingResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 功能：日志
 * @author sunpeng
 * @date 2017
 */
@Slf4j
@Aspect
@Configuration
public class SysLogAspect {

    private long startTime = 0; // 开始时间

    private final List<String> noLogList = Arrays.asList(
//        "com.szhl.sxtx.web.controller.mobile.OrderMobileController.changeOrderStatus",
    );

    @Pointcut("execution (* com.rose.controler..*.*(..))")
    private void aspectMethod() {

    }

    @Before(value = "aspectMethod()")
    public void before(JoinPoint point) throws Exception {
        startTime = System.currentTimeMillis();   //获取开始时间
        String method = point.getSignature().getDeclaringTypeName() + "." + point.getSignature().getName();
        if (!noLogList.contains(method)) {
            List param = null;
            if (point.getArgs() != null && point.getArgs().length > 0) {
                param = new ArrayList(point.getArgs().length);
                for (Object p : point.getArgs()) {
                    if (!(p instanceof HttpServletRequest) && !(p instanceof HttpServletResponse) && !(p instanceof BindingResult)) {
                        param.add(p);
                    }
                }
            }
            log.info("日志：请求---method：{}---param：{}", method, JsonUtil.objectToJson(param));
        }
    }

    @AfterReturning(value = "aspectMethod()", returning = "returnValue")
    public void after(JoinPoint point, Object returnValue) {
        String method = point.getSignature().getDeclaringTypeName() + "." + point.getSignature().getName();
        if (!noLogList.contains(method)) {
            log.info("日志：返回---method：{}---return：{}，共耗时-{}-毫秒", method, JsonUtil.objectToJson(returnValue), System.currentTimeMillis() - startTime);
        }
    }
}