package com.capstone.wanf.error.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Builder
@Getter
public class ErrorResponse {
    private final LocalDateTime timestamp = LocalDateTime.now();

    private final int status;

    private final String code;

    private final String message;
}
