package com.capstone.wanf.comment.dto.response;

import com.capstone.wanf.profile.dto.response.ProfileResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "댓글 응답")
public record CommentResponse(
        @Schema(name = "댓글 ID", description = "댓글 ID", example = "1")
        Long id,
        @Schema(name = "댓글 내용", description = "댓글 내용", example = "댓글 내용")
        String content,
        @Schema(name = "댓글 작성자 프로필", description = "댓글 작성자 프로필", example = "댓글 작성자 프로필")
        ProfileResponse profile
) {
}

