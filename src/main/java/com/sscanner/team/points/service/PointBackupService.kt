package com.sscanner.team.points.service;

public interface PointBackupService {
    void backupPointsToMySQL();
    void resetDailyPointsInCache();
}
