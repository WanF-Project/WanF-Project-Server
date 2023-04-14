package com.capstone.wanf.error.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CustomErrorCode implements ErrorCode {    // 특정 도메인 사용
    // 400 - 잘못된 요청
    INVALID_VERIFICATION_CODE(HttpStatus.BAD_REQUEST, "인증번호를 다시 발급받아주세요."),

    // 401 - 인증 실패
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "아이디 또는 비밀번호를 잘못 입력했습니다."),

    // 404 - 요청받은 리소스를 찾을 수 없음
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 email로 가입된 유저가 존재하지 않습니다."),
    VERIFICATION_NOT_FOUND(HttpStatus.NOT_FOUND, "인증번호가 올바르지 않습니다."),
    PROFILE_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 ID의 프로필이 존재하지 않습니다."),
    MAJOR_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 ID의 전공이 존재하지 않습니다."),
    COURSE_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 ID의 수업이 존재하지 않습니다."),
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 ID의 게시글이 존재하지 않습니다."),
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 ID의 댓글이 존재하지 않습니다."),

    // 409 - 충돌
    DUPLICATE_RESOURCE(HttpStatus.CONFLICT, "이미 가입된 이메일입니다.");

    private final HttpStatus httpStatus;

    private final String message;
}
