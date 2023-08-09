package com.capstone.wanf.profile.dto.response;

import com.capstone.wanf.course.domain.entity.Major;
import com.capstone.wanf.profile.domain.entity.MBTI;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.Map;

@Builder
@Schema(description = "프로필 응답")
public record ProfileResponse(
        @Schema(description = "프로필 ID", example = "1")
        Long id,
        @Schema(description = "닉네임", example = "김코딩")
        String nickname,
        @Schema(description = "학번", example = "2019101234")
        Integer studentId,
        @Schema(description = "나이", example = "23")
        Integer age,
        @Schema(description = "연락처", example = "010-1234-5678")
        String contact,
        @Schema(description = "프로필 이미지 url", example = "https://cloudfront.net/images/1691596349298.png")
        String profileImage,
        @Schema(description = "성별", example = "남성")
        Map<String, String> gender,
        @Schema(description = "MBTI", example = "ISTJ")
        MBTI mbti,
        @Schema(description = "성격", example = "외향적인")
        Map<String, String> personalities,
        @Schema(description = "목표", example = "취업")
        Map<String, String> goals,
        @Schema(description = "전공", example = "컴퓨터공학과")
        Major major
) {
}
