package com.sscanner.team.points.redis;

import java.util.Set;

public interface PointRedisService {
    Integer getPoint(String userId);
    Integer getDailyPoint(String userId);
    void updatePoint(String userId, Integer point);
    void incrementPoint(String userId, Integer incrementValue);
    void incrementDailyPoint(String userId, Integer incrementValue);
    void decrementPoint(String userId, Integer decrementValue);
    void flagUserForBackup(String userId);
    void removeBackupFlag(String userId);
    void resetDailyPoints();
    Set<String> scanKeys(String pattern);
    Set<String> getFlaggedUsers();
}
