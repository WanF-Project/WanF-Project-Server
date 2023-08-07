package com.capstone.wanf.club.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "모임 입장 요청")
public record ClubPwdRequest(
        @Schema(description = "모임 id", example = "1")
        Long clubId,
        @Schema(description = "모임 입장 비밀번호", example = "0000")
        String password
) {
}