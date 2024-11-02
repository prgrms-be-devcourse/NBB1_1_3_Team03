package com.sscanner.team.points.dto;

import com.sscanner.team.user.entity.User;
import com.sscanner.team.points.entity.UserPoint;

import java.util.UUID;


public record PointUpdateDto(
        UUID userPointId,
        User user,
        Integer newPoint
) {
    public static PointUpdateDto of(UUID userPointId, User user, Integer newPoint) {
        return new PointUpdateDto(userPointId, user, newPoint);
    }

    public UserPoint toEntity() {
        return UserPoint.builder()
                .id(userPointId)
                .user(user)
                .point(newPoint)
                .build();
    }
}
