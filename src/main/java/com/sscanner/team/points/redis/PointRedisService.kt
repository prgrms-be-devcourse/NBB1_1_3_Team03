package com.sscanner.team.points.redis

interface PointRedisService {
    fun getPoint(userId: String): Int
    fun getDailyPoint(userId: String): Int
    fun updatePoint(userId: String, point: Int)
    fun incrementPoint(userId: String, incrementValue: Int)
    fun incrementDailyPoint(userId: String, incrementValue: Int)
    fun decrementPoint(userId: String, decrementValue: Int)
    fun flagUserForBackup(userId: String)
    fun removeBackupFlag(userId: String)
    fun resetDailyPoints()
    fun scanKeys(pattern: String): Set<String>
    val flaggedUsers: Set<String>
}
