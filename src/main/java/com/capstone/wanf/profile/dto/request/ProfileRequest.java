package com.capstone.wanf.profile.dto.request;

import com.capstone.wanf.profile.domain.entity.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;


import java.util.List;


@Builder
@Schema(description = "프로필 생성 요청")
public record ProfileRequest(
        @Schema(description = "프로필 이미지", example = "BEAR")
        ProfileImage profileImage,
        @Schema(description = "닉네임", example = "닉네임")
        String nickname,
        @Schema(description = "전공 ID", example = "1")
        Long majorId,
        @Schema(description = "학번", example = "201814141")
        Integer studentId,
        @Schema(description = "나이", example = "23")
        Integer age,
        @Schema(implementation = Gender.class, description = "성별", example = "MALE")
        Gender gender,
        @Schema(implementation = MBTI.class, description = "MBTI", example = "INTJ")
        MBTI mbti,
        @Schema(implementation = Personality.class, description = "성격", example = "[ \"BRIGHT\" ]")
        List<Personality> personalities,
        @Schema(implementation = Goal.class, description = "목표", example = "[ \"GRADUATE\" ]")
        List<Goal> goals,
        @Schema(description = "연락 방법", example = "연락 방법")
        String contact
) {
}
