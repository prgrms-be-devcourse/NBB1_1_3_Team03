package com.sscanner.team.global.configure.aop;

import com.sscanner.team.global.common.response.ApiResponse;
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
import java.util.Objects;
import java.util.UUID;

@Component
@Aspect
@Slf4j
public class HttpLoggingAspect {

    private long startTime;

    // Controller의 모든 메서드에 대해 적용
    @Pointcut("execution(* com.sscanner.team..controller.*.*(..))")
    public void pointCut() {
    }

    @Before("pointCut()")
    public void logHttpRequest(JoinPoint joinPoint) {
        MDC.put("traceId", UUID.randomUUID().toString()); // 멀티 스레드 환경에서도 로그를 구분할 수 있게 해줌
        startTime = System.currentTimeMillis();
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        log.info("HTTP Request: HTTPMethod={} Path={} from IP={}", request.getMethod(), request.getRequestURI(), request.getRemoteAddr());
    }

    // 메서드 호출 후 정상적으로 반환된 경우 로그 남기기
    @AfterReturning(pointcut = "pointCut()", returning = "response")
    public void logAfterReturning(JoinPoint joinPoint, ApiResponse<?> response) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        log.info("HTTP Response: Path={} ResponseCode={} ResponseMessage={} ResponseData=\"{}\""
                , request.getRequestURI(), response.getCode(), response.getMessage(), response.getData());
    }

    // 예외 발생 시 로그 남기기
    @AfterThrowing(pointcut = "pointCut()", throwing = "ex")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable ex) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        // 특정 비즈니스 예외 처리: Todo: 이런 비즈니스 예외에 대한 부모클래스를 선언해서 코드를 깔끔하게 하기
        if (ex instanceof BadRequestException badRequestEx) {
            log.warn("HTTP Response: Path={} ResponseCode={} ResponseMessage=\"{}\" ResponseData=\"{}\""
                    , request.getRequestURI(), badRequestEx.getCode(), badRequestEx.getMessage(), null, ex);
        } else if (ex instanceof DuplicateException duplicateEx) {
            log.warn("HTTP Response: Path={} ResponseCode={} ResponseMessage=\"{}\" ResponseData=\"{}\""
                    , request.getRequestURI(), duplicateEx.getCode(), duplicateEx.getMessage(), null, ex);
        } else if (ex instanceof MethodArgumentNotValidException) {
            String errMessage = Objects.requireNonNull(((MethodArgumentNotValidException) ex).getBindingResult().getFieldError()).getDefaultMessage();
            log.warn("HTTP Response: Path={} ResponseCode=400 ResponseMessage=\"{}\" ResponseData=\"{}\""
                    , request.getRequestURI(), errMessage, null, ex);
        } else {
            // 그 외의 예외는 기본적인 에러 로그로 처리
            log.error("HTTP Response: Path={} ResponseCode=500 ResponseMessage=\"{}\" ResponseData=\"{}\""
                    , request.getRequestURI(), ex.getMessage(), null, ex);
        }
    }


    // 완전히 종료된후 메서드 실행시간 측정하기
    @After("pointCut()")
    public void logAfter(JoinPoint joinPoint) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        long executionTime = System.currentTimeMillis() - startTime;
        log.info("HTTP Execution: Path={} ExecutionTime={}ms", request.getRequestURI(), executionTime);
        MDC.clear();
    }

}
