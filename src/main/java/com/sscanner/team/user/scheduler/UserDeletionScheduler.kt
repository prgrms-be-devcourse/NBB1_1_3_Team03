package com.sscanner.team.user.scheduler

import com.sscanner.team.user.repository.UserRepository
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class UserDeletionScheduler(
    private val userRepository: UserRepository
) {

    @Scheduled(cron = "0 0 0 * * ?")
    fun deleteOldUsers() {
        val date = LocalDateTime.now().minusDays(30)

        // 기간 지난 사용자 물리적 삭제
        userRepository.deleteAllByDeletedAtBefore(date)
    }
}
