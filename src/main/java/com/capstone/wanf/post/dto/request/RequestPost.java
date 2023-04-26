package com.capstone.wanf.post.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "게시글 생성 요청")
public class RequestPost {
    @Schema(description = "게시글 제목", example = "게시글 제목")
    private String title;

    @Schema(description = "게시글 내용", example = "게시글 내용")
    private String content;

    @Schema(description = "수업 ID", example = "10")
    private Long courseId;
}
