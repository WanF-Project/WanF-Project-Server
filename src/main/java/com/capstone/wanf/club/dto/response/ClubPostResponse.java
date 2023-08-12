package com.capstone.wanf.club.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
@Schema(name = "ClubPostResponse", description = "모임 게시글 응답")
public record ClubPostResponse(
        @Schema(name = "id", description = "게시글 ID", example = "1")
        Long id,
        @Schema(name = "createdDate", description = "게시글 작성 시간", example = "2023-08-07 22:04:48.225898")
        LocalDateTime createdDate,
        @Schema(name = "modifiedDate", description = "게시글 수정 시간", example = "2023-08-07 22:13:40.646225")
        LocalDateTime modifiedDate,
        @Schema(name = "content", description = "게시글 내용", example = "교수님 전달사항입니다.")
        String content,
        @Schema(name = "nickname", description = "게시글 작성자 닉네임", example = "원프")
        String nickname,
        @Schema(name = "imageUrl", description = "게시글 이미지", example = "https://d1csu9i9ktup9e.cloudfront.net/default.png")
        String imageUrl
) {
}
