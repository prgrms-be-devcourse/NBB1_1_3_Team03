package com.sscanner.team.points.scheduler

import com.sscanner.team.points.service.PointBackupService
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class PointScheduler(private val pointBackupService: PointBackupService) {

    // 매일 오전 3시에 MySQL로 포인트를 백업합니다.
    @Scheduled(cron = "0 0 3 * * ?")
    fun backupPointsToMySQL() {
        pointBackupService.backupPointsToMySQL()
    }

    // 매일 자정에 캐시된 일일 포인트 제한을 초기화합니다.
    @Scheduled(cron = "0 0 0 * * ?")
    fun resetDailyPointLimit() {
        pointBackupService.resetDailyPointsInCache()
    }
}
