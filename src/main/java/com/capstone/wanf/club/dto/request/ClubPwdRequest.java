package com.capstone.wanf.club.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "모임 입장 요청")
public record ClubPwdRequest(
        @Schema(description = "모임 입장 비밀번호", example = "0000")
        String password
) {
}