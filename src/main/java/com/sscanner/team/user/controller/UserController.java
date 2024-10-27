package com.sscanner.team.user.controller;

import com.sscanner.team.global.common.response.ApiResponse;
import com.sscanner.team.global.exception.BadRequestException;
import com.sscanner.team.global.exception.ExceptionCode;
import com.sscanner.team.user.requestdto.*;
import com.sscanner.team.user.responsedto.*;
import com.sscanner.team.user.service.UserServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/users")
public class UserController {

    private final UserServiceImpl userServiceImpl;


    // 회원가입 기능
    @PostMapping("/join")
    public ApiResponse<UserJoinResponseDto> registerUSer(@RequestBody @Valid UserJoinRequestDto requestDTO) {
         userServiceImpl.join(requestDTO);
        return ApiResponse.ok(201, null, "회원가입 성공");
    }

    // 마이페이지
    @GetMapping("/my-page")
    public ApiResponse<UserMypageResponseDto> getMypage(){
        return userServiceImpl.getMypage();
    }

    // 닉네임 수정
    @PatchMapping("/change-nickname")
    public ApiResponse <UserNicknameUpdateResponseDto> updateNickname(@RequestBody UserNicknameUpdateRequestDto requestDto) {
        UserNicknameUpdateResponseDto responseDto = userServiceImpl.updateNickname(requestDto.newNickname());
        return ApiResponse.ok(200, responseDto, "닉네임 수정 성공");

    }

    // 비밀번호 확인 (폰번호 수정 전 현재 비밀번호 확인 후 접근 가능)
    @PostMapping("/confirm-password")
    public ApiResponse <String> confirmPassword(@RequestBody String password) {
        boolean isConfirmed = userServiceImpl.confirmPassword(password);
        if(isConfirmed){
            return ApiResponse.ok(200,null,"비밀번호 확인 성공");
        }else throw new BadRequestException(ExceptionCode.CURRENT_PASSWORD_NOT_MATCH);
    }

    // 핸드폰 번호 수정 요청
    @PatchMapping("/change-phone")
    public ApiResponse <UserPhoneUpdateResponseDto> updatePhoneNumber(@Valid @RequestBody UserPhoneUpdateRequestDto requestDto) {
        UserPhoneUpdateResponseDto responseDto = userServiceImpl.updatePhoneNumber(requestDto);
        return ApiResponse.ok(responseDto,"핸드폰 번호가 수정되었습니다.");
    }

    //비밀번호 수정
    @PatchMapping("/change-password")
    public ApiResponse<String> changePassword(@RequestBody UserPasswordChangeRequestDto requestDto) {
        String message = userServiceImpl.changePassword(requestDto);
        return ApiResponse.ok(message, "비밀번호 수정 성공");
    }

    //회원 탈퇴
    @DeleteMapping("/delete")
    public ApiResponse<Void> deleteUSer(){
        userServiceImpl.deleteUser();
        return ApiResponse.ok(200, null,"회원 탈퇴 성공");
    }

    //아이디 찾기
    @PostMapping("/find-id")
    public ApiResponse<UserFindIdResponseDto> findId(@Valid @RequestBody UserFindIdRequestDto requestDto) {
        UserFindIdResponseDto responseDto = userServiceImpl.findUserId(requestDto);
        return ApiResponse.ok(200, responseDto,"아이디 찾기 성공");
    }

    // 비밀번호 찾기
    @PostMapping("/reset-password")
    public ApiResponse<String> resetPassword(@RequestBody UserResetPasswordRequestDto requestDto){
        userServiceImpl.resetPassword(requestDto);
        return ApiResponse.ok( 200,null, "비밀번호가 성공적으로 변경되었습니다.");
    }
}


