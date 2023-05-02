package com.capstone.wanf.common.aop;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class ExecutionTimer {
    @Pointcut("@annotation(com.capstone.wanf.common.annotation.ExeTimer)")
    private void timer(){}

    @Around("timer()")
    public Object executeTime(ProceedingJoinPoint joinPoint) throws Throwable {

        StopWatch stopWatch = new StopWatch();


        stopWatch.start();

        Object[] args = joinPoint.getArgs();

        Object proceed = joinPoint.proceed(args);

        stopWatch.stop();

        long totalTimeMillis = stopWatch.getStopTime() - stopWatch.getStartTime();

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();

        String methodName = signature.getMethod().getName();

        log.info("실행 메서드: {}, 실행시간 = {}ms", methodName, totalTimeMillis);

        return proceed;
    }
}
