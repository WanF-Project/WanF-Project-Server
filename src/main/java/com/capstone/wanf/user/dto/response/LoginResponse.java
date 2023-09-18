package com.capstone.wanf.user.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "로그인 응답")
public record LoginResponse(
        @Schema(description = "유저 프로필 id", example = "1")
        Long profileId
) {
}
