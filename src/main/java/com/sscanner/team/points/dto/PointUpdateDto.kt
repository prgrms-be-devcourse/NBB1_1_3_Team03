package com.sscanner.team.points.dto

import com.sscanner.team.points.entity.UserPoint
import com.sscanner.team.user.entity.User
import java.util.*

data class PointUpdateDto(
    val userPointId: UUID,
    val user: User,
    val newPoint: Int
) {
    fun toEntity(): UserPoint {
        return UserPoint.create(
            id = userPointId,
            user = user,
            point = newPoint
        )
    }

    companion object {
        fun of(userPointId: UUID, user: User, newPoint: Int): PointUpdateDto {
            return PointUpdateDto(userPointId, user, newPoint)
        }
    }
}
