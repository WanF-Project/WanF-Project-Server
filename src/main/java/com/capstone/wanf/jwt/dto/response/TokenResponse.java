package com.capstone.wanf.jwt.dto.response;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class TokenResponse {
    private String accessToken;

    private String refreshToken;
}
