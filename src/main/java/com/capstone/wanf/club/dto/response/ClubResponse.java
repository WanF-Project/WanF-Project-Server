package com.capstone.wanf.club.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "모임 요청에 대한 응답")
public record ClubResponse(
        @Schema(description = "모임 id", example = "1")
        Long id,
        @Schema(description = "모임명", example = "캡디 A+ 모임")
        String name,
        @Schema(description = "모집 인원수", example = "3")
        int maxParticipants
) {
}