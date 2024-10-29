package com.sscanner.team.user.service;

import com.sscanner.team.global.common.response.ApiResponse;
import com.sscanner.team.user.requestdto.*;
import com.sscanner.team.user.responsedto.*;

public interface UserService {

    UserJoinResponseDto join(UserJoinRequestDto req);

    ApiResponse<UserMypageResponseDto> getMypage();

    boolean confirmPassword(String password);

    UserPhoneUpdateResponseDto updatePhoneNumber(UserPhoneUpdateRequestDto req);

    UserNicknameUpdateResponseDto updateNickname(String newNickname);

    String changePassword(UserPasswordChangeRequestDto requestDto);

    void deleteUser();

    UserFindIdResponseDto findUserId(UserFindIdRequestDto requestDto);

    void resetPassword(UserResetPasswordRequestDto requestDto);

}
