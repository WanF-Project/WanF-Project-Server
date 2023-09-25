package com.capstone.wanf.post.dto.response;

import com.capstone.wanf.course.dto.response.CoursePaginationResponse;
import com.capstone.wanf.post.domain.entity.Post;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
@Schema(description = "게시글 모두 조회 응답 데이터")
public record PostPaginationResponse(
        @Schema(name = "게시글 ID", description = "게시글 ID", example = "1")
        Long id,
        @Schema(name = "강의", description = "강의", example = "강의")
        CoursePaginationResponse course,
        @Schema(name = "게시글 제목", description = "게시글 제목", example = "게시글 제목")
        String title,
        @Schema(name = "게시글 작성날짜", description = "게시글 작성날짜", example = "2021-10-01 00:00:00")
        LocalDateTime createdDate,
        @Schema(name = "게시글 수정날짜", description = "게시글 수정날짜", example = "2021-10-01 00:00:00")
        LocalDateTime modifiedDate
) {
        public static PostPaginationResponse of(Post post) {
                return PostPaginationResponse.builder()
                        .id(post.getId())
                        .course(CoursePaginationResponse.of(post.getCourse()))
                        .title(post.getTitle())
                        .createdDate(post.getCreatedDate())
                        .modifiedDate(post.getModifiedDate())
                        .build();
        }
}
