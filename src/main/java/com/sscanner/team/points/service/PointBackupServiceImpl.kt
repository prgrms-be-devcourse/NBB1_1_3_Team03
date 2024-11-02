package com.sscanner.team.points.service;

import com.sscanner.team.points.entity.UserPoint;
import com.sscanner.team.points.dto.PointUpdateDto;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import static com.sscanner.team.points.common.PointConstants.*;
@Service
@Slf4j
@RequiredArgsConstructor
public class PointBackupServiceImpl implements PointBackupService {

    private final PointService pointService;
    private final ThreadPoolTaskExecutor pointTaskExecutor;
    private final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(5);
    private final ReentrantLock lock = new ReentrantLock();

    @Override
    public void backupPointsToMySQL() {
        Set<String> flaggedUsers = pointService.getFlaggedUsersForBackup();
        runBackupAsyncForUsers(flaggedUsers);
    }

    private void runBackupAsyncForUsers(Set<String> flaggedUsers) {
        for (String flaggedUserId : flaggedUsers) {
            String userId = flaggedUserId.replace(BACKUP_FLAG_PREFIX, "");
            CompletableFuture.runAsync(() -> processPointBackup(userId), pointTaskExecutor)
                    .exceptionally(ex -> {
                        log.error("Backup failed for user {}", userId, ex);
                        return null;
                    });
        }
    }

    private void processPointBackup(String userId) {
        retryAsync(() -> backupUserPoints(userId), RETRY_MAX_ATTEMPTS, RETRY_DELAY);
    }

    private void retryAsync(Runnable task, int attempts, long delay) {
        CompletableFuture.runAsync(() -> {
            try {
                task.run();
            } catch (Exception e) {
                handleRetryFailure(task, attempts, delay, e);
            }
        }, pointTaskExecutor);
    }

    private void handleRetryFailure(Runnable task, int attempts, long delay, Exception e) {
        if (attempts > 0) {
            log.error("Attempt failed, retrying... Attempts left: {}, delay: {}ms", attempts, delay, e);
            scheduledExecutorService.schedule(() -> retryAsync(task, attempts - 1, delay * 2), delay, TimeUnit.MILLISECONDS);
        } else {
            log.error("Max retry attempts reached. Task failed.");
        }
    }

    private void backupUserPoints(String userId) {
        Integer currentCachedPoint = pointService.getPoint(userId);
        if (currentCachedPoint != null) {
            updateUserPointsWithLock(userId, currentCachedPoint);
        }
        removeBackupFlagWithLock(userId);
    }

    private void updateUserPointsWithLock(String userId, Integer currentCachedPoint) {
        lock.lock();
        try {
            PointUpdateDto updateDto = createPointUpdateRequestDto(userId, currentCachedPoint);
            pointService.updateUserPoint(updateDto.toEntity());
        } finally {
            lock.unlock();
        }
    }

    private PointUpdateDto createPointUpdateRequestDto(String userId, Integer currentCachedPoint) {
        UserPoint userPoint = pointService.findByUserId(userId);
        return PointUpdateDto.of(userPoint.getId(), userPoint.getUser(), currentCachedPoint);
    }

    private void removeBackupFlagWithLock(String userId) {
        lock.lock();
        try {
            pointService.removeBackupFlag(userId);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void resetDailyPointsInCache() {
        pointService.resetDailyPointsInCache();
    }

    @PreDestroy
    public void shutdown() {
        pointTaskExecutor.shutdown();
        try {
            if (!hasTaskExecutorTerminated(pointTaskExecutor)) {
                pointTaskExecutor.getThreadPoolExecutor().shutdownNow();
            }
        } catch (InterruptedException ex) {
            pointTaskExecutor.getThreadPoolExecutor().shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    private boolean hasTaskExecutorTerminated(ThreadPoolTaskExecutor taskExecutor) throws InterruptedException {
        return taskExecutor.getThreadPoolExecutor().awaitTermination(30, TimeUnit.SECONDS);
    }
}
