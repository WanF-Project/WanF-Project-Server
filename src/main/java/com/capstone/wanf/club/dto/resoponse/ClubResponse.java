package com.capstone.wanf.club.dto.resoponse;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@Schema(description = "모임 요청에 대한 응답")
public class ClubResponse {
    @Schema(description = "모임 id", example = "1")
    private Long id;

    @Schema(description = "모임명", example = "캡디 A+ 모임")
    private String name;

    @Schema(description = "모임 인원수", example = "3")
    private int maxParticipants;
}
