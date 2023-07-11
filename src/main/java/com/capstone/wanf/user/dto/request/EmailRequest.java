package com.capstone.wanf.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "인증번호 생성 요청")
public record EmailRequest(
        @Schema(description = "이메일", example = "wanf@office.skhu.ac.kr")
        String email
) {
}
