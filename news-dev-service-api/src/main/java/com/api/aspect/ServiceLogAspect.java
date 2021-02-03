package com.api.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * service日志切面
 *
 * @author Json
 * @date 2021/1/20 21:12
 */
@Aspect
@Component
public class ServiceLogAspect {

    private final static Logger logger = LoggerFactory.getLogger(ServiceLogAspect.class);

    private final Integer ERROR_TAKE_TIME = 3000;
    private final Integer WARN_TAKE_TIME = 2000;

    /**
     * AOP通知:
     * 1.前置通知
     * 2.后置通知
     * 3.环绕通知
     * 4.异常通知
     * 5.最终通知
     */

    @Around("execution(* com.*.service.impl..*.*(..))")
    public Object recordTimeOfService(ProceedingJoinPoint joinPoint) throws Throwable {

        logger.info("===== 开始执行 {}.{} =====", joinPoint.getTarget().getClass(), joinPoint.getSignature().getName());

        long startTime = System.currentTimeMillis();

        Object result = joinPoint.proceed();

        long endTime = System.currentTimeMillis();
        long takeTime = endTime - startTime;

        if (takeTime > ERROR_TAKE_TIME) {
            logger.error("当前执行耗时:{}", takeTime);
        } else if (takeTime > WARN_TAKE_TIME) {
            logger.warn("当前执行耗时:{}", takeTime);
        } else {
            logger.info("当前执行耗时:{}", takeTime);
        }

        return result;
    }


}
