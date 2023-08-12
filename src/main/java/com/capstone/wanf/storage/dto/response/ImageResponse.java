package com.capstone.wanf.storage.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "이미지 업로드 응답")
public record ImageResponse(
        @Schema(description = "이미지 URL", example = "https://d1csu9i9ktup9e.cloudfront.net/default.png")
        String imageUrl
) {
}
