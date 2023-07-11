package com.capstone.wanf.user.dto.response;

import com.capstone.wanf.user.domain.entity.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "유저 응답")
public record UserResponse(
        @Schema(description = "유저 id", example = "1")
        Long id,

        @Schema(description = "이메일", example = "wanf@office.skhu.ac.kr")
        String email,

        @Schema(description = "권한", example = "일반 사용자")
        Role role
) {
}
