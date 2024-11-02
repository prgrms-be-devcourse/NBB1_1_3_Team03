package com.sscanner.team.points.service

interface PointBackupService {
    fun backupPointsToMySQL()
    fun resetDailyPointsInCache()
}
