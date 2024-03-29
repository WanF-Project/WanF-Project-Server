package com.capstone.wanf.profile.dto.response;

import com.capstone.wanf.course.domain.entity.Major;
import com.capstone.wanf.profile.domain.entity.Goal;
import com.capstone.wanf.profile.domain.entity.MBTI;
import com.capstone.wanf.profile.domain.entity.Personality;
import com.capstone.wanf.profile.domain.entity.Profile;
import com.capstone.wanf.storage.dto.response.ImageResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.Map;
import java.util.stream.Collectors;

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
        @Schema(description = "게시글 이미지 정보")
        ImageResponse image,
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
        public static ProfileResponse of(Profile profile) {
                return ProfileResponse.builder()
                        .id(profile.getId())
                        .nickname(profile.getNickname())
                        .studentId(profile.getStudentId())
                        .age(profile.getAge())
                        .image(ImageResponse.of(profile.getImage()))
                        .gender(Map.of(profile.getGender().name(), profile.getGender().getGender()))
                        .mbti(profile.getMbti())
                        .personalities(profile.getPersonalities().stream()
                                .collect(Collectors
                                        .toMap(Personality::name, Personality::getDetail)))
                        .goals(profile.getGoals().stream()
                                .collect(Collectors
                                        .toMap(Goal::name, Goal::getDetail)))
                        .major(profile.getMajor())
                        .build();
        }
}
