package com.sscanner.team.points.redis;

import com.sscanner.team.points.entity.UserPoint;
import com.sscanner.team.points.repository.PointRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static com.sscanner.team.points.common.PointConstants.POINT_PREFIX;

@Component
@Slf4j
@RequiredArgsConstructor
public class RedisDataInitializer implements CommandLineRunner {

    private final PointRepository pointRepository;
    private final PointRedisService pointRedisService;
    private final ExecutorService executorService = Executors.newFixedThreadPool(10);

    @Override
    public void run(String... args) {
        // 비동기적으로 Redis 초기화 작업을 처리
        executorService.submit(this::initializeRedisData);
    }

    private void initializeRedisData() {
        try {
            Set<String> keys = pointRedisService.scanKeys(POINT_PREFIX + "*");
            if (keys.isEmpty()) {
                processPagedData();
            }
        } catch (Exception e) {
            log.error("Error during Redis data initialization", e);
        } finally {
            shutdownExecutorService();
        }
    }

    private void processPagedData() {
        int pageSize = 1000;  // 배치 크기 설정
        int pageNumber = 0;
        Page<UserPoint> userPoints = pointRepository.findAllWithUser(PageRequest.of(pageNumber, pageSize));

        while (!userPoints.isEmpty()) {
            List<UserPoint> userPointList = userPoints.getContent();
            for (UserPoint userPoint : userPointList) {
                processUserPointAsync(userPoint);
            }

            pageNumber++;
            userPoints = pointRepository.findAllWithUser(PageRequest.of(pageNumber, pageSize));
        }
    }

    private void processUserPointAsync(UserPoint userPoint) {
        executorService.submit(() -> {
            try {
                String userId = userPoint.getUser().getUserId();
                Integer point = userPoint.getPoint();
                pointRedisService.updatePoint(userId, point);
            } catch (Exception e) {
                log.error("Error updating Redis for userId {}", userPoint.getUser().getUserId(), e);
            }
        });
    }

    private void shutdownExecutorService() {
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException ex) {
            log.error("Error shutting down ExecutorService", ex);
            Thread.currentThread().interrupt();
        }
    }
}
