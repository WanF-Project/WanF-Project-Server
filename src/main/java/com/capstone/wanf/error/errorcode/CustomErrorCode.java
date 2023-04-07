package com.capstone.wanf.error.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CustomErrorCode implements ErrorCode {    // 특정 도메인 사용
    DUPLICATE_RESOURCE(HttpStatus.CONFLICT, "이미 가입된 이메일입니다.");
//    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "아이디 또는 비밀번호를 잘못 입력했습니다.")

    private final HttpStatus httpStatus;

    private final String message;
}
