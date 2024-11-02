package com.sscanner.team.points.redis;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static com.sscanner.team.points.common.PointConstants.*;


@Slf4j
@Service
@RequiredArgsConstructor
public class PointRedisServiceImpl implements PointRedisService {

    private final RedisTemplate<String, Integer> redisTemplate;

    @Override
    public Integer getPoint(String userId) {
        return redisTemplate.opsForValue().get(getKey(userId));
    }

    @Override
    public Integer getDailyPoint(String userId) {
        return Optional.ofNullable(redisTemplate.opsForValue().get(getDailyKey(userId))).orElse(0);
    }

    @Override
    public void updatePoint(String userId, Integer point) {
        redisTemplate.opsForValue().set(getKey(userId), point, 1, TimeUnit.DAYS);
    }

    @Override
    public void incrementPoint(String userId, Integer incrementValue) {
        redisTemplate.opsForValue().increment(getKey(userId), incrementValue);
    }

    @Override
    public void incrementDailyPoint(String userId, Integer incrementValue) {
        redisTemplate.opsForValue().increment(getDailyKey(userId), incrementValue);
    }

    @Override
    public void decrementPoint(String userId, Integer decrementValue) {
        redisTemplate.opsForValue().decrement(getKey(userId), decrementValue);
    }

    @Override
    public void flagUserForBackup(String userId) {
        redisTemplate.opsForValue().set(BACKUP_FLAG_PREFIX + userId, 1);
    }

    @Override
    public void removeBackupFlag(String userId) {
        redisTemplate.delete(BACKUP_FLAG_PREFIX + userId);
    }

    @Override
    public void resetDailyPoints() {
        ScanOptions scanOptions = ScanOptions.scanOptions().match(DAILY_POINT_PREFIX + "*").build();

        try (Cursor<String> cursor = redisTemplate.scan(scanOptions)) {
            while (cursor.hasNext()) {
                String key = cursor.next();
                redisTemplate.delete(key);
            }
        } catch (Exception e) {
            log.error("Error during Redis SCAN operation", e);
        }
    }

    @Override
    public Set<String> scanKeys(String pattern) {
        Set<String> keys = new HashSet<>();
        ScanOptions scanOptions = ScanOptions.scanOptions().match(pattern).build();

        try (Cursor<String> cursor = redisTemplate.scan(scanOptions)) {
            while (cursor.hasNext()) {
                keys.add(cursor.next());
            }
        } catch (Exception e) {
            log.error("Error during Redis SCAN operation", e);
        }
        return keys;
    }

    @Override
    public Set<String> getFlaggedUsers() {
        return redisTemplate.keys(BACKUP_FLAG_PREFIX + "*");
    }

    private String getKey(String userId) {
        return POINT_PREFIX + userId;
    }

    private String getDailyKey(String userId) {
        return DAILY_POINT_PREFIX + userId;
    }
}
