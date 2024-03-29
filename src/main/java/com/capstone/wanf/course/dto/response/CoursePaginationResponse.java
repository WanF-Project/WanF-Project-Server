package com.capstone.wanf.course.dto.response;

import com.capstone.wanf.course.domain.entity.Course;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "강의 모두 조회 강의 응답 데이터")
public record CoursePaginationResponse(
        @Schema(name = "강의 ID", description = "강의 ID", example = "1")
        Long id,
        @Schema(name = "강의 이름", description = "강의 이름", example = "강의 이름")
        String name,
        @Schema(name = "교수명", description = "교수명", example = "교수명")
        String professor
) {
        public static CoursePaginationResponse of(Course course) {
                return CoursePaginationResponse.builder()
                        .id(course.getId())
                        .name(course.getName())
                        .professor(course.getProfessor())
                        .build();
        }
}
