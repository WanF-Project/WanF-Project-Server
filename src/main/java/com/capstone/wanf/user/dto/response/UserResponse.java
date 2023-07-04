package com.capstone.wanf.user.dto.response;

import com.capstone.wanf.user.domain.entity.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "유저 응답")
public class UserResponse {
    @Schema(description = "유저 id", example = "1")
    private Long id;

    @Schema(description = "이메일", example = "wanf@office.skhu.ac.kr")
    private String email;

    @Schema(description = "권한", example = "일반 사용자")
    private Role role;
}
