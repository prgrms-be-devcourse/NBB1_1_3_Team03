package com.sscanner.team.user.service;

import com.sscanner.team.global.exception.BadRequestException;
import com.sscanner.team.global.exception.DuplicateException;
import com.sscanner.team.global.exception.ExceptionCode;
import com.sscanner.team.user.repository.UserRepository;
import com.sscanner.team.user.requestDto.UserJoinRequestDto;
import com.sscanner.team.user.responseDto.UserJoinResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    // 이메일 중복 체크
    public void checkEmail(String email) {
        userRepository.findByEmail(email)
                .ifPresent(user -> {
                    throw new DuplicateException(ExceptionCode.DUPLICATED_EMAIL);
                });
    }

    //닉네임 중복 체크
    public void checkNickname(String nickname) {
        userRepository.findByNickname(nickname)
                .ifPresent(user -> {
                    throw new DuplicateException(ExceptionCode.DUPLICATED_NICKNAME);
                });
    }

    //핸드폰 중복 체크
    public void checkPhone(String phone) {
        userRepository.findByPhone(phone)
                .ifPresent(user -> {
                    throw new DuplicateException(ExceptionCode.DUPLICATED_PHONE);
                });
    }

    // 비밀번호 확인용 메서드
    public void confirmPassword(String password, String passwordCheck) {
        if(!password.equals(passwordCheck)) {
            throw new BadRequestException(ExceptionCode.PASSWORD_NOT_MATCH);
        }
    }

    // 회원가입
    public UserJoinResponseDto join(UserJoinRequestDto req){

        checkEmail(req.email());
        checkNickname(req.nickname());

        checkPhone(req.phone());
        confirmPassword(req.password(), req.passwordCheck());

        var userEntity = req.toEntity(passwordEncoder.encode(req.password()));
        userRepository.save(userEntity);

        return UserJoinResponseDto.from(userEntity);
    }


}


