package com.capstone.wanf.profile.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
@Schema(description = "이미지를 포함한 프로필 요청")
public record ProfileImageRequest(
        @NotNull(message = "프로필 이미지는 null이 될 수 없습니다.")
        @Schema(description = "이미지 id", example = "1")
        Long imageId,
        @Schema(implementation = ProfileRequest.class, description = "프로필 나머지 데이터")
        ProfileRequest profileRequest
) {
}
