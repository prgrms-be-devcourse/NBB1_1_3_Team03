package com.sscanner.team.points.service

import com.sscanner.team.points.dto.requestdto.PointRequestDto
import com.sscanner.team.points.dto.responsedto.PointWithUserIdResponseDto
import com.sscanner.team.points.entity.UserPoint

interface PointService {
    fun getCachedPoint(userId: String): PointWithUserIdResponseDto
    fun addPoint(pointRequestDto: PointRequestDto): PointWithUserIdResponseDto
    fun fetchCachedPoint(userId: String): Int
    fun findByUserId(userId: String): UserPoint
    fun markUserForBackup(userId: String)
    fun updateUserPoint(userPoint: UserPoint)
    fun removeBackupFlag(userId: String)
    fun decrementPoint(userId: String, productPrice: Int)
    val flaggedUsersForBackup: Set<String>
    fun getPoint(userId: String): Int
    fun resetDailyPointsInCache()
}
