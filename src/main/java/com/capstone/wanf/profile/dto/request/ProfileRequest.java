package com.capstone.wanf.profile.dto.request;

import com.capstone.wanf.profile.domain.entity.*;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Builder;
import org.hibernate.validator.constraints.Length;


import java.util.List;


@Builder
@Schema(description = "프로필 생성 요청")
public record ProfileRequest(
        @NotNull(message = "이미지는 필수입니다.")
        @Schema(description = "프로필 이미지", example = "BEAR")
        ProfileImage profileImage,
        @NotBlank(message = "공백이나 null이 될 수 없습니다.")
        @Length(min = 1, max = 20)
        @Schema(description = "닉네임", example = "닉네임")
        String nickname,
        @Schema(description = "전공 ID", example = "1")
        Long majorId,
        @Max(value = 202399999, message = "잘못된 학번입니다.")
        @Min(value = 200000000, message = "잘못된 학번입니다.")
        @Schema(description = "학번", example = "201814141")
        Integer studentId,
        @Min(value = 19, message = "19세 이상이여야 합니다.")
        @Max(value = 150, message = "100세 이하여야 합니다.")
        @Schema(description = "나이", example = "23")
        Integer age,
        @NotNull(message = "성별을 입력하세요")
        @Schema(implementation = Gender.class, description = "성별", example = "MALE")
        Gender gender,
        @NotNull(message = "MBTI를 입력하세요")
        @Schema(implementation = MBTI.class, description = "MBTI", example = "INTJ")
        MBTI mbti,
        @NotNull(message = "성격을 1개 이상 선택하세요")
        @Schema(implementation = Personality.class, description = "성격", example = "[ \"BRIGHT\" ]")
        List<Personality> personalities,
        @NotNull(message = "목표를 1개 이상 선택하세요")
        @Schema(implementation = Goal.class, description = "목표", example = "[ \"GRADUATE\" ]")
        List<Goal> goals,
        @NotBlank(message = "연락 방법을 입력하세요")
        @Length(max = 255, message = "연락 방법은 255글자 이내여야 합니다.")
        @Schema(description = "연락 방법", example = "연락 방법")
        String contact
) {
}
