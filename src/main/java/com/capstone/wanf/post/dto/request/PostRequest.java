package com.capstone.wanf.post.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "게시글 생성 요청")
public record PostRequest(
        @Schema(description = "게시글 제목", example = "게시글 제목")
        String title,
        @Schema(description = "게시글 내용", example = "게시글 내용")
        String content,
        @Schema(description = "강의 ID", example = "10")
        Long courseId
) {
}
