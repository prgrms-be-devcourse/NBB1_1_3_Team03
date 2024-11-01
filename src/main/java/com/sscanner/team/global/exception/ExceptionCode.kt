package com.sscanner.team.global.exception;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ExceptionCode {

    /* 예시 코드
    //Bad Request
    NOT_FOUND_ORDER_ID(400, "취소되었거나 존재하지 않는 주문입니다."),

    NOT_EXIST_FILE(400, "파일이 비어있습니다."),
    FILE_UPLOAD_FAIL(400,  "파일 업로드를 실패하였습니다."),

    //Unauthorized
    INVALID_PASSWORD(401, "이메일이나 비밀번호가 일치하지 않습니다."),
    INVALID_EMAIL(401, "이메일이나 비밀번호가 일치하지 않습니다."),

    //conflict
    DUPLICATED_PRODUCT_NAME(409, "이미 존재하는 상품명입니다."),

    //Unsupported Media Type

    BAD_FILE_EXTENSION(415, "적절하지 않은 파일 확장자입니다.");
    DUPLICATED_PRODUCT_NAME(409, "이미 존재하는 상품명입니다.");
    */

    //Unsupported Media Type
    BAD_FILE_EXTENSION(415, "적절하지 않은 파일 확장자입니다."),
    INVALID_FILE_NAME(415, "파일 이름이 잘못되었습니다."),

    //BAD REQUEST
    NOT_EXIST_TRASHCAN_ID(400, "해당하는 쓰레기통이 존재하지 않습니다."),
    NOT_EXIST_FILE(400, "파일이 비어있습니다."),
    NOT_EXIST_BOARD(400, "해당하는 게시글이 존재하지 않습니다."),
    NOT_EXIST_BOARD_IMG(400, "해당하는 게시글의 이미지가 존재하지 않습니다."),
    NOT_EXIST_COMMENT(400, "해당하는 댓글이 존재하지 않습니다."),
    NOT_FOUND_PRODUCT_ID(400, "해당하는 상품이 존재하지 않습니다."),
    NOT_FOUND_USER_ID(400, "해당 사용자를 찾을 수 없습니다."),
    NOT_FOUND_ITEM_ID(400, "해당 주문에 상품이 존재하지 않습니다."),
    NOT_FOUND_PRODUCT_IMG_ID(400, "해당 주문에 대한 상품 이미지가 존재하지 않습니다."),
    NOT_ENOUGH_POINTS(400, "사용 가능한 포인트가 부족합니다."),
    DAILY_POINTS_EXCEEDED(400, "일일 획득 포인트를 초과했습니다."),
    FILE_UPLOAD_FAIL(400,  "파일 업로드를 실패하였습니다."),
    MISMATCH_BOARD_TYPE(400, "신고 게시글 유형이 적절하지 않습니다"),
    MISMATCH_AUTHOR(400, "작성자가 일치하지 않습니다."),

    DUPLICATED_EMAIL(409, "이미 가입된 이메일입니다. "),
    DUPLICATED_NICKNAME(409, "이미 가입된 닉네임입니다. "),
    DUPLICATED_PHONE(409, "이미 가입된 핸드폰 번호입니다. "),
    PASSWORD_NOT_MATCH(400, "비밀번호와 비밀번호 확인이 일치하지 않습니다."),
    USER_NOT_FOUND(400,"해당 유저가 존재하지 않습니다."),
    NOT_EXIST_TRASHCAN_IMG_ID(400, "해당하는 쓰레기통 이미지가 존재하지 않습니다."),
    NOT_FOUND_NEARBY_TRASHCANS(404, "근처에 쓰레기통이 존재하지 않습니다."),
    NOT_EXIST_REFRESH_TOKEN(400, "리프레시 토큰이 존재하지 않습니다."),
    EXPIRED_REFRESH_TOKEN(400, "리프레시 토큰이 만료되었습니다."),
    INVALID_REFRESH_TOKEN(400, "유효하지 않은 리프레시 토큰입니다."),
    CURRENT_PASSWORD_NOT_MATCH(400, "현재 비밀번호와 일치하지 않습니다."),
    UNAUTHORIZED(401, "인증에 실패했습니다."),
    SAME_PHONE_NUMBER(400,"입력한 번호가 현재 등록된 번호와 동일합니다."),
    PHONE_VERIFICATION_FAILED(400, "핸드폰 인증에 실패하였습니다."),
    USER_NOT_FOUND_BY_PHONE(400,"해당 번호로 가입된 사용자가 없습니다."),
    INSTANCE_NOT_ALLOWED(500, "이 클래스는 인스턴스화할 수 없습니다.");
    ;

    private final int code;
    private final String message;
}
