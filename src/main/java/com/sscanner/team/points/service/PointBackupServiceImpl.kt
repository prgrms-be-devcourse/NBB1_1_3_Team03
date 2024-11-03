package com.sscanner.team.points.service

import com.sscanner.team.points.common.PointConstants
import com.sscanner.team.points.dto.PointUpdateDto
import jakarta.annotation.PreDestroy
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import org.springframework.stereotype.Service
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit
import java.util.concurrent.locks.ReentrantLock

@Service
class PointBackupServiceImpl(
    private val pointService: PointService,
    private val pointTaskExecutor: ThreadPoolTaskExecutor
) : PointBackupService {
    private val scheduledExecutorService: ScheduledExecutorService = Executors.newScheduledThreadPool(5)
    private val lock = ReentrantLock()

    override fun backupPointsToMySQL() {
        val flaggedUsers = pointService.flaggedUsersForBackup
        runBackupAsyncForUsers(flaggedUsers)
    }

    private fun runBackupAsyncForUsers(flaggedUsers: Set<String>) {
        flaggedUsers.forEach { userId ->
            val cleanUserId = userId.replace(PointConstants.BACKUP_FLAG_PREFIX, "")
            CompletableFuture.runAsync({ processPointBackup(cleanUserId) }, pointTaskExecutor)
                .exceptionally { ex ->
                    log.error("Backup failed for user {}", cleanUserId, ex)
                    null
                }
        }
    }

    private fun processPointBackup(userId: String) {
        retryAsync(
            { backupUserPoints(userId) },
            PointConstants.RETRY_MAX_ATTEMPTS,
            PointConstants.RETRY_DELAY
        )
    }

    private fun retryAsync(task: Runnable, attempts: Int, delay: Long) {
        CompletableFuture.runAsync({
            try {
                task.run()
            } catch (e: Exception) {
                handleRetryFailure(task, attempts, delay, e)
            }
        }, pointTaskExecutor)
    }

    private fun handleRetryFailure(task: Runnable, attempts: Int, delay: Long, e: Exception) {
        if (attempts > 0) {
            log.error("Attempt failed, retrying... Attempts left: {}, delay: {}ms", attempts, delay, e)
            scheduledExecutorService.schedule(
                { retryAsync(task, attempts - 1, delay * 2) },
                delay,
                TimeUnit.MILLISECONDS
            )
        } else {
            log.error("Max retry attempts reached. Task failed.")
        }
    }

    private fun backupUserPoints(userId: String) {
        val currentCachedPoint = pointService.getPoint(userId)
        updateUserPointsWithLock(userId, currentCachedPoint)
        removeBackupFlagWithLock(userId)
    }

    private fun updateUserPointsWithLock(userId: String, currentCachedPoint: Int) {
        lock.lock()
        try {
            val userPoint = pointService.findByUserId(userId)
            userPoint.let {
                val updateDto = PointUpdateDto.of(
                    userPointId = it.id,
                    user = it.user,
                    newPoint = currentCachedPoint
                )
                pointService.updateUserPoint(updateDto.toEntity())
            }
        } finally {
            lock.unlock()
        }
    }

    private fun removeBackupFlagWithLock(userId: String) {
        lock.lock()
        try {
            pointService.removeBackupFlag(userId)
        } finally {
            lock.unlock()
        }
    }

    override fun resetDailyPointsInCache() {
        pointService.resetDailyPointsInCache()
    }

    @PreDestroy
    fun shutdown() {
        pointTaskExecutor.shutdown()
        try {
            if (!hasTaskExecutorTerminated(pointTaskExecutor)) {
                pointTaskExecutor.threadPoolExecutor.shutdownNow()
            }
        } catch (ex: InterruptedException) {
            pointTaskExecutor.threadPoolExecutor.shutdownNow()
            Thread.currentThread().interrupt()
        }
    }

    private fun hasTaskExecutorTerminated(taskExecutor: ThreadPoolTaskExecutor): Boolean {
        return taskExecutor.threadPoolExecutor.awaitTermination(30, TimeUnit.SECONDS)
    }

    companion object {
        private val log: Logger = LoggerFactory.getLogger(PointBackupServiceImpl::class.java)
    }
}
