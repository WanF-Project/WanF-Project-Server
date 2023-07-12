package com.capstone.wanf.club.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "모임 입장 요청")
public class ClubPwdRequest {
    @Schema(description = "모임 입장 비밀번호", example = "0000")
    private String password;
}
