package com.sscanner.team.global.configure.aop;

import com.sscanner.team.global.exception.BadRequestException;
import com.sscanner.team.global.exception.DuplicateException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;
import java.util.UUID;

@Component
@Aspect
@Slf4j
public class ServiceLogAspect {

    private long startTime;

    // service의 모든 메서드에 대해 적용
    @Pointcut("execution(* com.sscanner.team..service.*.*(..))")
    public void pointCut() {}

    // 메서드 호출 전 로그 남기기
    @Before("pointCut()")
    public void logBefore(JoinPoint joinPoint) {
        startTime = System.currentTimeMillis();
        log.info("Entering Service: Method={} with Args={}", ((MethodSignature) joinPoint.getSignature()).getMethod().getName(), Arrays.toString(joinPoint.getArgs()));
    }

    // 메서드 호출 후 정상적으로 반환된 경우 로그 남기기
    @AfterReturning(pointcut = "pointCut()", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        log.info("Exiting Service: Method={} with Return={}", ((MethodSignature) joinPoint.getSignature()).getMethod().getName(), result);
    }

    // 완전히 종료된후 메서드 실행시간 측정하기
    @After("pointCut()")
    public void logAfter(JoinPoint joinPoint) {
        long executionTime = System.currentTimeMillis() - startTime;
        log.info("Method={} ExecutionTime={}ms", ((MethodSignature) joinPoint.getSignature()).getMethod().getName(), executionTime);
    }
}
