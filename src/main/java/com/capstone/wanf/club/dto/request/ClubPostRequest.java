package com.capstone.wanf.club.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "모임 게시글 생성 요청")
public record ClubPostRequest(
        @Schema(description = "게시글 내용", example = "교수님 공지입니다.")
        String content
) {
}