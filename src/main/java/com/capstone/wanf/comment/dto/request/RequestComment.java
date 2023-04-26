package com.capstone.wanf.comment.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "댓글 생성 요청")
public class RequestComment {
    @Schema(description = "댓글 내용", example = "댓글 내용")
    private String content;
}
