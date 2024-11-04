package com.sscanner.team.auth.responsedto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RefreshResponseDto {
    private String accessToken;
    private String refreshToken;

    public static RefreshResponseDto of(String accessToken, String refreshToken) {
        return new RefreshResponseDto(accessToken, refreshToken);
    }
}