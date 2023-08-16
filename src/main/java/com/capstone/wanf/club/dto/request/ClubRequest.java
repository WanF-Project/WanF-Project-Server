package com.capstone.wanf.club.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(description = "모임 생성 요청")
public record ClubRequest(
        @NotBlank
        @Size(min = 1, max = 50)
        @Schema(description = "모임명", example = "캡디 A+ 모임")
        String name,
        @Schema(description = "모집 인원수", example = "3")
        Integer maxParticipants,
        @Pattern(regexp = "^[a-zA-Z0-9]{4,8}$", message = "모임 입장 비밀번호는 영문/숫자 4~8자리여야 합니다.")
        @Schema(description = "모임 입장 비밀번호(영문/숫자 4~8자리)", example = "bk30")
        String password
) {
}