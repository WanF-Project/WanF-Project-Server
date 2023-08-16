package com.capstone.wanf.club.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

@Builder
@Schema(description = "모임 입장 요청")
public record ClubPwdRequest(
        @Schema(description = "모임 id", example = "1")
        Long clubId,
        @Pattern(regexp = "^[a-zA-Z0-9]{4,8}$", message = "모임 입장 비밀번호는 영문/숫자 4~8자리여야 합니다.")
        @Schema(description = "모임 입장 비밀번호(영문/숫자 4~8자리)", example = "bk30")
        String password
) {
}