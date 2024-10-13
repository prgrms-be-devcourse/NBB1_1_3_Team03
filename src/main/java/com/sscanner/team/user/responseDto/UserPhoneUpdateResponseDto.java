package com.sscanner.team.user.responseDto;

import com.sscanner.team.User;

public record UserPhoneUpdateResponseDto(
        String userId,
        String newPhone
) {
    public static UserPhoneUpdateResponseDto from(User user) {
        return new UserPhoneUpdateResponseDto(
                user.getUserId(),
                user.getPhone()
        );
    }
}
