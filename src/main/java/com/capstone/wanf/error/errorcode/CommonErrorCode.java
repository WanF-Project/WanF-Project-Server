package com.capstone.wanf.error.errorcode;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CommonErrorCode implements ErrorCode {  // 애플리케이션 전역 사용
    INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),

    NOT_FOUND(HttpStatus.NOT_FOUND, "해당 데이터를 찾을 수 없습니다."),

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "내부 서버 에러"),
    
    TRANSACTION_FAILED(HttpStatus.CONFLICT, "트랜잭션 커밋을 시도하는 동안 예외가 발생하여 변경에 실패했습니다.");

    private final HttpStatus httpStatus;

    private final String message;
}
