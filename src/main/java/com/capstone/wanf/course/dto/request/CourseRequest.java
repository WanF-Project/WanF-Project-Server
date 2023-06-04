package com.capstone.wanf.course.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "CourseRequest", description = "수업 생성 요청")
public class CourseRequest {
    @Schema(description = "수업 이름", example = "수업 이름")
    private String name;

    @Schema(description = "수업 카테고리", example = "수업 카테고리")
    private String category;

    @Schema(description = "수업 시간", example = "수업 시간")
    private String courseTime;

    @Schema(description = "수업 코드", example = "수업 코드")
    private String courseId;

    @Schema(description = "교수", example = "교수")
    private String professor;

    @Schema(description = "전공 ID", example = "1")
    private Long majorId;

    public CourseRequest(String name, String category, String courseTime, String courseId, String professor) {
        this.name = name;
        this.category = category;
        this.courseTime = courseTime;
        this.courseId = courseId;
        this.professor = professor;
    }
}
