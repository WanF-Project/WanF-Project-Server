package com.capstone.wanf.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "인증번호 검증 요청")
public class CodeRequest {
    @Schema(description = "이메일", example = "wanf@office.skhu.ac.kr")
    private String email;

    @Schema(description = "인증번호", example = "3AWC94")
    private String verificationCode;
}
