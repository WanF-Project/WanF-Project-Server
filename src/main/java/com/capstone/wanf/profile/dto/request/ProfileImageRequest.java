package com.capstone.wanf.profile.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "이미지를 포함한 프로필 요청")
public record ProfileImageRequest(
        @Schema(description = "이미지 URL", example = "https://d1csu9i9ktup9e.cloudfront.net/default.png")
        String profileImageUrl,
        @Schema(implementation = ProfileRequest.class, description = "프로필 나머지 데이터")
        ProfileRequest profileRequest
) {
}
