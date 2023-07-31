package com.capstone.wanf.error.handler;


import com.capstone.wanf.error.dto.response.ErrorResponse;
import com.capstone.wanf.error.errorcode.CommonErrorCode;
import com.capstone.wanf.error.errorcode.CustomErrorCode;
import com.capstone.wanf.error.errorcode.ErrorCode;
import com.capstone.wanf.error.exception.RestApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    // 커스텀 예외 처리
    @ExceptionHandler(RestApiException.class)
    public ResponseEntity<Object> handleException(final RestApiException e) {
        final ErrorCode errorCode = e.getErrorCode();

        return handleExceptionInternal(errorCode);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Object> handleBadCredentialsException() {
        final ErrorCode errorCode = CustomErrorCode.UNAUTHORIZED;

        return handleExceptionInternal(errorCode);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> handleAccessDeniedException() {
        final ErrorCode errorCode = CommonErrorCode.FORBIDDEN;

        return handleExceptionInternal(errorCode);
    }

    // 트랜젝션 예외 처리
    @ExceptionHandler(TransactionSystemException.class)
    public ResponseEntity<Object> handleTransactionSystemException(final TransactionSystemException e) {
        log.warn("handleTransactionSystemException", e);

        final ErrorCode errorCode = CommonErrorCode.TRANSACTION_FAILED;

        return handleExceptionInternal(errorCode);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        BindingResult bindingResult = e.getBindingResult();

        String errorMessage = bindingResult.getFieldErrors().stream()
                .map(fieldError -> "[" + fieldError.getField() + "](은)는 " + fieldError.getDefaultMessage() +
                        " 입력된 값: [" + fieldError.getRejectedValue() + "]")
                .collect(Collectors.joining());

        final CommonErrorCode errorCode = CommonErrorCode.METHOD_ARGUMENT_NOT_VALID;

        return handleExceptionInternal(errorCode, errorMessage);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> handleDataIntegrityViolationException(final DataIntegrityViolationException e) {
        log.warn("handleDataIntegrityViolationException", e);

        final ErrorCode errorCode = CommonErrorCode.DATA_INTEGRITY_VIOLATION;

        return handleExceptionInternal(errorCode);
    }

   // 예상하지 못한 서버 에러 처리
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAllException(final Exception ex) {
        log.warn("handleAllException", ex);

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
