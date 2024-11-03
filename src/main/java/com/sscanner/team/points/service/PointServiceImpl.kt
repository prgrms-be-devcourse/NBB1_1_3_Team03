package com.sscanner.team.points.service

import com.sscanner.team.global.exception.BadRequestException
import com.sscanner.team.global.exception.ExceptionCode
import com.sscanner.team.points.common.PointConstants
import com.sscanner.team.points.dto.requestdto.PointRequestDto
import com.sscanner.team.points.dto.responsedto.PointWithUserIdResponseDto
import com.sscanner.team.points.entity.UserPoint
import com.sscanner.team.points.redis.PointRedisService
import com.sscanner.team.points.repository.PointRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PointServiceImpl(private val pointRedisService: PointRedisService, private val pointRepository: PointRepository) :
    PointService {
    override fun getCachedPoint(userId: String): PointWithUserIdResponseDto {
        val point = fetchAndCacheUserPoint(userId)
        return PointWithUserIdResponseDto.of(userId, point)
    }

    @Transactional
    override fun addPoint(pointRequestDto: PointRequestDto): PointWithUserIdResponseDto {
        val userId = pointRequestDto.userId
        val point = pointRequestDto.point

        fetchAndCacheUserPoint(userId)

        validateDailyLimitPoints(userId, point)

        updateRedisPoints(userId, point)

        markUserForBackup(userId)

        val updatedPoint = fetchCachedPoint(userId)
        return PointWithUserIdResponseDto.of(userId, updatedPoint)
    }

    override fun fetchCachedPoint(userId: String): Int {
        return pointRedisService.getPoint(userId)
    }

    override fun findByUserId(userId: String): UserPoint {
        return pointRepository.findByUserId(userId)
            .orElseThrow {
                BadRequestException(
                    ExceptionCode.NOT_FOUND_USER_ID
                )
            }
    }

    override fun markUserForBackup(userId: String) {
        pointRedisService.flagUserForBackup(userId)
    }

    @Transactional
    override fun updateUserPoint(userPoint: UserPoint) {
        pointRepository.save(userPoint)
    }

    override fun removeBackupFlag(userId: String) {
        pointRedisService.removeBackupFlag(userId)
    }

    override fun decrementPoint(userId: String, productPrice: Int) {
        pointRedisService.decrementPoint(userId, productPrice)
    }

    override val flaggedUsersForBackup: Set<String>
        get() = pointRedisService.flaggedUsers

    override fun getPoint(userId: String): Int {
        return pointRedisService.getPoint(userId)
    }

    override fun resetDailyPointsInCache() {
        pointRedisService.resetDailyPoints()
    }

    private fun fetchAndCacheUserPoint(userId: String): Int {
        var point = fetchCachedPoint(userId)

        if (isPointAbsent(point)) {
            point = loadUserPoint(userId)
            cacheUserPoint(userId, point)
        }
        return point
    }

    private fun isPointAbsent(point: Int?): Boolean {
        return point == null
    }

    private fun loadUserPoint(userId: String): Int {
        return findByUserId(userId).point
    }

    private fun cacheUserPoint(userId: String, point: Int) {
        pointRedisService.updatePoint(userId, point)
    }

    private fun validateDailyLimitPoints(userId: String, point: Int) {
        val dailyPoint = pointRedisService.getDailyPoint(userId)
        if (dailyPoint + point > PointConstants.DAILY_LIMIT) {
            throw BadRequestException(ExceptionCode.DAILY_POINTS_EXCEEDED)
        }
    }

    private fun updateRedisPoints(userId: String, point: Int) {
        pointRedisService.incrementPoint(userId, point)
        pointRedisService.incrementDailyPoint(userId, point)
    }
}
