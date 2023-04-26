package com.capstone.wanf.profile.dto.request;

import com.capstone.wanf.profile.domain.entity.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


import java.util.List;

@Data
@Schema(description = "프로필 생성 요청")
public class ProfileRequest {
    @Schema(description = "프로필 이미지", example = "프로필 이미지")
    private ProfileImage profileImage;

    @Schema(description = "닉네임", example = "닉네임")
    private String nickname;

    @Schema(description = "전공 ID", example = "1")
    private Long majorId;

    @Schema(description = "학번", example = "학번")
    private Integer studentId;

    @Schema(description = "나이", example = "나이")
    private Integer age;

    @Schema(implementation = Gender.class, description = "성별", example = "MALE")
    private Gender gender;

    @Schema(implementation = MBTI.class, description = "MBTI", example = "INTJ")
    private MBTI mbti;

    @Schema(implementation = Personality.class, description = "성격", example = "성격")
    private List<Personality> personalities;

    @Schema(implementation = Goal.class, description = "목표", example = "목표")
    private List<Goal> goals;

    @Schema(description = "연락 방법", example = "연락 방법")
    private String contact;
}
