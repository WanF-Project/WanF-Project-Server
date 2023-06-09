package com.capstone.wanf.error.errorcode;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CommonErrorCode implements ErrorCode {  // 애플리케이션 전역 사용
    // 401 - 인증 실패
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "인증 토큰이 유효하지 않습니다."),

    // 403 - 접근 권한 없음
    FORBIDDEN(HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),

    // 409 - 충돌
    TRANSACTION_FAILED(HttpStatus.CONFLICT, "트랜잭션 커밋을 시도하는 동안 예외가 발생하여 변경에 실패했습니다."),

    // 500 - 서버 에러
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "내부 서버 에러"),
    DATA_INTEGRITY_VIOLATION(HttpStatus.INTERNAL_SERVER_ERROR, "데이터 무결성 위반(한 유저는 하나의 프로필만을 사용할 수 있습니다.)");

    private final HttpStatus httpStatus;

    private final String message;
}
