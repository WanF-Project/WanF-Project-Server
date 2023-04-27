package com.capstone.wanf.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "회원가입 요청")
public class UserRequest {
    @Schema(description = "이메일", example = "wanf@office.skhu.ac.kr")
    private String email;

    @Schema(description = "비밀번호", example = "qwer1234")
    private String userPassword;
}
