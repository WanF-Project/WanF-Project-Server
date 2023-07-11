package com.capstone.wanf.post.dto.response;

import com.capstone.wanf.comment.dto.response.CommentResponse;
import com.capstone.wanf.course.domain.entity.Course;
import com.capstone.wanf.profile.dto.response.ProfileResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
@Builder
@Schema(name = "게시글 응답", description = "게시글 응답")
public record PostResponse(
        @Schema(name = "게시글 ID", description = "게시글 ID", example = "1")
        Long id,
        @Schema(name = "게시글 제목", description = "게시글 제목", example = "게시글 제목")
        String title,
        @Schema(name = "게시글 내용", description = "게시글 내용", example = "게시글 내용")
        String content,
        @Schema(name ="게시글 카테고리", description = "게시글 카테고리", example = "게시글 카테고리")
        Map<String,String> category,
        @Schema(name = "어떤 강의의 게시물", description = "어떤 강의의 게시물", example = "어떤 강의의 게시물")
        Course course,
        @Schema(name = "게시글 작성자 프로필", description = "게시글 작성자 프로필", example = "게시글 작성자 프로필")
        ProfileResponse profile,
        @Schema(name = "게시글 작성날짜", description = "게시글 작성날짜", example = "2021-10-01 00:00:00")
        LocalDateTime createdDate,
        @Schema(name = "게시글 수정날짜", description = "게시글 수정날짜", example = "2021-10-01 00:00:00")
        LocalDateTime modifiedDate,
        @Schema(name = "댓글 목록", description = "댓글 목록", example = "댓글 목록")
        List<CommentResponse> comments
){

}
