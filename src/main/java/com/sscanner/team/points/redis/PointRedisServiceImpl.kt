package com.sscanner.team.points.redis

import com.sscanner.team.points.common.PointConstants
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.ScanOptions
import org.springframework.stereotype.Service
import java.util.concurrent.TimeUnit

@Service
class PointRedisServiceImpl(private val redisTemplate: RedisTemplate<String, Int>) : PointRedisService {

    override fun getPoint(userId: String): Int {
        return redisTemplate.opsForValue()[getKey(userId)]!!
    }

    override fun getDailyPoint(userId: String): Int {
        return redisTemplate.opsForValue()[getDailyKey(userId)] ?: 0
    }

    override fun updatePoint(userId: String, point: Int) {
        redisTemplate.opsForValue().set(getKey(userId), point, 1, TimeUnit.DAYS)
    }

    override fun incrementPoint(userId: String, incrementValue: Int) {
        redisTemplate.opsForValue().increment(getKey(userId), incrementValue.toLong())
    }

    override fun incrementDailyPoint(userId: String, incrementValue: Int) {
        redisTemplate.opsForValue().increment(getDailyKey(userId), incrementValue.toLong())
    }

    override fun decrementPoint(userId: String, decrementValue: Int) {
        redisTemplate.opsForValue().decrement(getKey(userId), decrementValue.toLong())
    }

    override fun flagUserForBackup(userId: String) {
        redisTemplate.opsForValue().set(PointConstants.BACKUP_FLAG_PREFIX + userId, 1)
    }

    override fun removeBackupFlag(userId: String) {
        redisTemplate.delete(PointConstants.BACKUP_FLAG_PREFIX + userId)
    }

    override fun resetDailyPoints() {
        val scanOptions = ScanOptions.scanOptions().match("${PointConstants.DAILY_POINT_PREFIX}*").build()
        try {
            redisTemplate.scan(scanOptions).use { cursor ->
                cursor.forEach { key -> redisTemplate.delete(key) }
            }
        } catch (e: Exception) {
            log.error("Error during Redis SCAN operation", e)
        }
    }

    override fun scanKeys(pattern: String): Set<String> {
        val keys = mutableSetOf<String>()
        val scanOptions = ScanOptions.scanOptions().match(pattern).build()
        try {
            redisTemplate.scan(scanOptions).use { cursor ->
                cursor.forEach { key -> keys.add(key) }
            }
        } catch (e: Exception) {
            log.error("Error during Redis SCAN operation", e)
        }
        return keys
    }

    override val flaggedUsers: Set<String>
        get() = redisTemplate.keys("${PointConstants.BACKUP_FLAG_PREFIX}*") ?: emptySet()

    private fun getKey(userId: String): String {
        return "${PointConstants.POINT_PREFIX}$userId"
    }

    private fun getDailyKey(userId: String): String {
        return "${PointConstants.DAILY_POINT_PREFIX}$userId"
    }

    companion object {
        private val log: Logger = LoggerFactory.getLogger(PointRedisServiceImpl::class.java)
    }
}
