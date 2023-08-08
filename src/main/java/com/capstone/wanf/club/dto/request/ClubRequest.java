package com.capstone.wanf.club.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "모임 생성 요청")
public record ClubRequest(
        @Schema(description = "모임명", example = "캡디 A+ 모임")
        String name,
        @Schema(description = "모집 인원수", example = "3")
        int maxParticipants,
        @Schema(description = "모임 입장 비밀번호", example = "0000")
        String password
) {
}