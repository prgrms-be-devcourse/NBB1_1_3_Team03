package com.sscanner.team.points.scheduler;


import com.sscanner.team.points.service.PointBackupService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PointScheduler {

    private final PointBackupService pointBackupService;

    @Scheduled(cron = "0 0 3 * * ?")
    public void backupPointsToMySQL() {
        pointBackupService.backupPointsToMySQL();
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void resetDailyPointLimit() {
        pointBackupService.resetDailyPointsInCache();
    }
}
