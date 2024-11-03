package com.sscanner.team.points.redis

import com.sscanner.team.points.common.PointConstants
import com.sscanner.team.points.entity.UserPoint
import com.sscanner.team.points.repository.PointRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Component
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

@Component
class RedisDataInitializer(
    private val pointRepository: PointRepository,
    private val pointRedisService: PointRedisService
) : CommandLineRunner {
    private val executorService: ExecutorService = Executors.newFixedThreadPool(10)

    override fun run(vararg args: String) {
        // 비동기적으로 Redis 초기화 작업을 처리
        executorService.submit { this.initializeRedisData() }
    }

    private fun initializeRedisData() {
        try {
            val keys = pointRedisService.scanKeys(PointConstants.POINT_PREFIX + "*")
            if (keys.isEmpty()) {
                processPagedData()
            }
        } catch (e: Exception) {
            log.error("Error during Redis data initialization", e)
        } finally {
            shutdownExecutorService()
        }
    }

    private fun processPagedData() {
        val pageSize = 1000 // 배치 크기 설정
        var pageNumber = 0
        var userPoints = pointRepository.findAllWithUser(PageRequest.of(pageNumber, pageSize))

        while (!userPoints.isEmpty) {
            val userPointList = userPoints.content
            for (userPoint in userPointList) {
                if (userPoint != null) {
                    processUserPointAsync(userPoint)
                }
            }

            pageNumber++
            userPoints = pointRepository.findAllWithUser(PageRequest.of(pageNumber, pageSize))
        }
    }

    private fun processUserPointAsync(userPoint: UserPoint) {
        executorService.submit {
            try {
                val userId = userPoint.user.userId
                val point = userPoint.point
                if (userId != null) {
                    pointRedisService.updatePoint(userId, point)
                }
            } catch (e: Exception) {
                log.error("Error updating Redis for userId {}", userPoint.user.userId, e)
            }
        }
    }

    private fun shutdownExecutorService() {
        executorService.shutdown()
        try {
            if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                executorService.shutdownNow()
            }
        } catch (ex: InterruptedException) {
            log.error("Error shutting down ExecutorService", ex)
            Thread.currentThread().interrupt()
        }
    }

    companion object {
        private val log: Logger = LoggerFactory.getLogger(RedisDataInitializer::class.java)
    }
}
