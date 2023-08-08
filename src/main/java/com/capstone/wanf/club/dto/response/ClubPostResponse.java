package com.capstone.wanf.club.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(name = "ClubPostResponse", description = "모임 게시글 응답")
public record ClubPostResponse(
        @Schema(name = "id", description = "게시글 ID", example = "1")
        Long id,
        @Schema(name = "content", description = "게시글 내용", example = "교수님 전달사항입니다.")
        String content,
        @Schema(name = "nickname", description = "게시글 작성자 닉네임", example = "원프")
        String nickname
) {
}
