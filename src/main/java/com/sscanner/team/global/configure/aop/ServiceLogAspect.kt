package com.sscanner.team.global.configure.aop

import lombok.extern.slf4j.Slf4j
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.*
import org.aspectj.lang.reflect.MethodSignature
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
@Aspect
class ServiceLogAspect {
    private var startTime: Long = 0
    private val log = LoggerFactory.getLogger(this.javaClass)!!

    // service의 모든 메서드에 대해 적용
    @Pointcut("execution(* com.sscanner.team..service.*.*(..))")
    fun pointCut() {
    }

    // 메서드 호출 전 로그 남기기
    @Before("pointCut()")
    fun logBefore(joinPoint: JoinPoint) {
        startTime = System.currentTimeMillis()
        log.info(
            "Entering Service: Method={} with Args={}",
            (joinPoint.signature as MethodSignature).method.name,
            joinPoint.args.contentToString()
        )
    }

    // 메서드 호출 후 정상적으로 반환된 경우 로그 남기기
    @AfterReturning(pointcut = "pointCut()", returning = "result")
    fun logAfterReturning(joinPoint: JoinPoint, result: Any?) {
        log.info(
            "Exiting Service: Method={} with Return={}",
            (joinPoint.signature as MethodSignature).method.name,
            result
        )
    }

    // 완전히 종료된후 메서드 실행시간 측정하기
    @After("pointCut()")
    fun logAfter(joinPoint: JoinPoint) {
        val executionTime = System.currentTimeMillis() - startTime
        log.info(
            "Method={} ExecutionTime={}ms",
            (joinPoint.signature as MethodSignature).method.name,
            executionTime
        )
    }
}
