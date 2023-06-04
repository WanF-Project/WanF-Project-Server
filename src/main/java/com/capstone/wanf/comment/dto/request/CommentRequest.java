package com.capstone.wanf.comment.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "댓글 생성 요청")
public record CommentRequest(
        @Schema(description = "댓글 내용", example = "댓글 내용")
        String content
) {
}
