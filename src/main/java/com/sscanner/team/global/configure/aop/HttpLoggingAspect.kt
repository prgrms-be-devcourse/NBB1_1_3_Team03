package com.sscanner.team.global.configure.aop

import com.sscanner.team.global.common.response.ApiResponse
import com.sscanner.team.global.exception.BadRequestException
import com.sscanner.team.global.exception.DuplicateException
import lombok.extern.slf4j.Slf4j
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.*
import org.slf4j.LoggerFactory
import org.slf4j.MDC
import org.springframework.stereotype.Component
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import java.util.*

@Component
@Aspect
class HttpLoggingAspect {
    private var startTime: Long = 0
    private val log = LoggerFactory.getLogger(this.javaClass)!!

    // Controller의 모든 메서드에 대해 적용
    @Pointcut("execution(* com.sscanner.team..controller.*.*(..))")
    fun pointCut() {
    }

    @Before("pointCut()")
    fun logHttpRequest(joinPoint: JoinPoint?) {
        MDC.put("traceId", UUID.randomUUID().toString()) // 멀티 스레드 환경에서도 로그를 구분할 수 있게 해줌
        startTime = System.currentTimeMillis()
        val request = (RequestContextHolder.currentRequestAttributes() as ServletRequestAttributes).request
        log.info(
            "HTTP Request: HTTPMethod={} Path={} from IP={}",
            request.method,
            request.requestURI,
            request.remoteAddr
        )
    }

    // 메서드 호출 후 정상적으로 반환된 경우 로그 남기기
    @AfterReturning(pointcut = "pointCut()", returning = "response")
    fun logAfterReturning(joinPoint: JoinPoint?, response: ApiResponse<*>) {
        val request = (RequestContextHolder.currentRequestAttributes() as ServletRequestAttributes).request
        log.info(
            "HTTP Response: Path={} ResponseCode={} ResponseMessage={} ResponseData=\"{}\"",
            request.requestURI, response.code, response.message, response.data
        )
    }

    // 예외 발생 시 로그 남기기
    @AfterThrowing(pointcut = "pointCut()", throwing = "ex")
    fun logAfterThrowing(joinPoint: JoinPoint?, ex: Throwable) {
        val request = (RequestContextHolder.currentRequestAttributes() as ServletRequestAttributes).request
        // 특정 비즈니스 예외 처리: Todo: 이런 비즈니스 예외에 대한 부모클래스를 선언해서 코드를 깔끔하게 하기
        if (ex is BadRequestException) {
            log.warn(
                "HTTP Response: Path={} ResponseCode={} ResponseMessage=\"{}\" ResponseData=\"{}\"",
                request.requestURI, ex.code, ex.message, null, ex
            )
        } else if (ex is DuplicateException) {
            log.warn(
                "HTTP Response: Path={} ResponseCode={} ResponseMessage=\"{}\" ResponseData=\"{}\"",
                request.requestURI, ex.code, ex.message, null, ex
            )
        } else if (ex is MethodArgumentNotValidException) {
            val errMessage = Objects.requireNonNull(ex.bindingResult.fieldError).defaultMessage
            log.warn(
                "HTTP Response: Path={} ResponseCode=400 ResponseMessage=\"{}\" ResponseData=\"{}\"",
                request.requestURI, errMessage, null, ex
            )
        } else {
            // 그 외의 예외는 기본적인 에러 로그로 처리
            log.error(
                "HTTP Response: Path={} ResponseCode=500 ResponseMessage=\"{}\" ResponseData=\"{}\"",
                request.requestURI, ex.message, null, ex
            )
        }
    }


    // 완전히 종료된후 메서드 실행시간 측정하기
    @After("pointCut()")
    fun logAfter(joinPoint: JoinPoint?) {
        val request = (RequestContextHolder.currentRequestAttributes() as ServletRequestAttributes).request
        val executionTime = System.currentTimeMillis() - startTime
        log.info("HTTP Execution: Path={} ExecutionTime={}ms", request.requestURI, executionTime)
        MDC.clear()
    }
}
