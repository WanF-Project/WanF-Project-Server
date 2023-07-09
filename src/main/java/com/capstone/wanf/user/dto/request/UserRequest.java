package com.capstone.wanf.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "회원가입 요청")
public record UserRequest(
        @Schema(description = "이메일", example = "wanf@office.skhu.ac.kr")
        String email,

        @Schema(description = "비밀번호", example = "qwer1234")
        String userPassword
) {
}
