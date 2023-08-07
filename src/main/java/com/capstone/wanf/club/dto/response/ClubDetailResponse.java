package com.capstone.wanf.club.dto.response;

import com.capstone.wanf.club.domain.entity.ClubPost;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.List;

@Builder
@Schema(description = "생성된 모임 응답")
public record ClubDetailResponse(
        @Schema(description = "모임 id", example = "1")
        Long id,
        @Schema(description = "모임명", example = "캡디 A+ 모임")
        String name,
        @Schema(description = "최대 인원 수", example = "5")
        int maxParticipants,
        @Schema(description = "현재 인원 수", example = "1")
        int currentParticipants,
        @Schema(description = "모임 비밀번호", example = "1234")
        String password,
        @Schema(description = "모집 상태", example = "true")
        boolean recruitmentStatus,
        @Schema(description = "모임 게시글", example = "모임 게시글 목록")
        List<ClubPost> posts
) {
}
