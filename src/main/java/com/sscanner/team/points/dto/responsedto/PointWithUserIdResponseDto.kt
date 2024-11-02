package com.sscanner.team.points.dto.responsedto;

public record PointWithUserIdResponseDto(
        String userId,
        Integer point
) {
    public static PointWithUserIdResponseDto of(String userId, Integer point) {
        return new PointWithUserIdResponseDto(userId, point);
    }
}
