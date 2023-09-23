package com.capstone.wanf.club.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
@Schema(description = "모임 게시글 생성 요청")
public record ClubPostRequest(
        @NotBlank(message = "공백이나 null이 될 수 없습니다.")
        @Schema(description = "게시글 내용", example = "교수님 공지입니다.")
        String content,
        @Schema(description = "이미지 id", example = "1")
        Long imageId
) {
}