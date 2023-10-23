package com.capstone.wanf.banner.dto.response;

import com.capstone.wanf.banner.domain.entity.Banner;
import com.capstone.wanf.storage.dto.response.ImageResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "배너 응답")
public record BannerResponse(
        @Schema(description = "배너 url", example = "https://github.com")
        String url,
        @Schema(description = "배너 이미지 정보")
        ImageResponse image
) {
    public static BannerResponse of(Banner banner) {
        return BannerResponse.builder()
                .url(banner.getUrl())
                .image(ImageResponse.of(banner.getImage()))
                .build();
    }
}
