package com.capstone.wanf.club.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "모임 생성 요청")
public class ClubRequest {
    @Schema(description = "모임명", example = "캡디 A+ 모임")
    private String name;

    @Schema(description = "모집 인원수", example = "3")
    private int maxParticipants;

    @Schema(description = "모임 입장 비밀번호", example = "0000")
    private String password;
}
