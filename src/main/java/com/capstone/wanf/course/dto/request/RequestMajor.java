package com.capstone.wanf.course.dto.request;

import com.capstone.wanf.course.domain.entity.Major;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "RequestMajor", description = "전공 생성 요청")
public class RequestMajor {
    @Schema(name = "name", description = "전공 이름", example = "컴퓨터공학과")
    private String name;

    public Major toEntity() {
        return Major.builder()
                .name(name)
                .build();
    }
}
