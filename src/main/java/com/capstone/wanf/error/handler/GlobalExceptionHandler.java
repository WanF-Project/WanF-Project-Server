package com.capstone.wanf.error.handler;


import com.capstone.wanf.error.dto.response.ErrorResponse;
import com.capstone.wanf.error.errorcode.CommonErrorCode;
import com.capstone.wanf.error.errorcode.ErrorCode;
import com.capstone.wanf.error.exception.RestApiException;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    // 커스텀 예외 처리
    @ExceptionHandler(RestApiException.class)
    public ResponseEntity<Object> handleException(final RestApiException e) {
        final ErrorCode errorCode = e.getErrorCode();

        return handleExceptionInternal(errorCode);
    }

    // 트랜젝션 예외 처리
    @ExceptionHandler(TransactionSystemException.class)
    public ResponseEntity<Object> handleTransactionSystemException(final TransactionSystemException ex) {
        final ErrorCode errorCode = CommonErrorCode.TRANSACTION_FAILED;

        return handleExceptionInternal(errorCode);
    }

    // 예상하지 못한 서버 에러 처리
    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleAllException(final Exception ex) {
        final ErrorCode errorCode = CommonErrorCode.INTERNAL_SERVER_ERROR;

        return handleExceptionInternal(errorCode, ex.getMessage());
    }

    // Method Overloading

    // ErrorCode를 전달받아, 해당 에러코드에 맞는 HTTP 응답 코드와 함께 ErrorResponse 객체를 반환
    private ResponseEntity<Object> handleExceptionInternal(final ErrorCode errorCode) {
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(makeResponseError(errorCode));
    }

    // ErrorCode를 전달받아, 해당 에러코드에 맞는 HTTP 응답 코드와 함께 ErrorResponse 객체를 생성
    private ErrorResponse makeResponseError(final ErrorCode errorCode) {
        return ErrorResponse.builder()
                .status(errorCode.getHttpStatus().value())
                .code(errorCode.name())
                .message(errorCode.getMessage())
                .build();
    }

    // ErrorCode와 String 메시지를 전달받아, 해당 에러코드와 메시지에 맞는 HTTP 응답 코드와 함께 ErrorResponse 객체를 반환
    private ResponseEntity<Object> handleExceptionInternal(final ErrorCode errorCode, final String message) {
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(makeResponseError(errorCode, message));
    }

    // makeErrorResponse 메소드는 ErrorCode와 String 메시지를 전달받아, ErrorResponse 객체를 생성
    private ErrorResponse makeResponseError(final ErrorCode errorCode, final String message) {
        return ErrorResponse.builder()
                .status(errorCode.getHttpStatus().value())
                .code(errorCode.name())
                .message(message)
                .build();
    }
}
