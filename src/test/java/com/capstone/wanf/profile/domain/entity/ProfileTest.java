package com.capstone.wanf.profile.domain.entity;

import com.capstone.wanf.profile.dto.request.ProfileRequest;
import com.capstone.wanf.profile.dto.response.ProfileResponse;
import com.capstone.wanf.storage.dto.response.ImageResponse;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.capstone.wanf.fixture.DomainFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.assertAll;

class ProfileTest {
    private static final String NICKNAME = "닉네임";

    private static final int STUDENT_ID = 201814100;

    private static final int AGE = 23;

    private static final String CONTACT = "010-1234-5678";

    private static final Gender GENDER = Gender.MALE;

    private static final MBTI MBTI = com.capstone.wanf.profile.domain.entity.MBTI.ENFJ;

    private static final List<Personality> PERSONALITY_LIST = List.of(Personality.BRAVERY, Personality.BRIGHT);

    private static List<Goal> GOAL_LIST = List.of(Goal.GRADUATE, Goal.AREA_OF_INTEREST);

    @Test
    void 프로필을_생성할_수_있다() {
        //when & then
        assertThatCode(() -> Profile.builder()
                .nickname(NICKNAME)
                .studentId(STUDENT_ID)
                .age(AGE)
                .image(이미지1)
                .gender(GENDER)
                .mbti(MBTI)
                .personalities(PERSONALITY_LIST)
                .goals(GOAL_LIST)
                .major(전공1)
                .user(유저1)
                .build())
                .doesNotThrowAnyException();
    }

    @Test
    void 프로필에_저장된_정보를_조회할_수_있다() {
        //given
        Profile profile = Profile.builder()
                .nickname(NICKNAME)
                .studentId(STUDENT_ID)
                .age(AGE)
                .image(이미지1)
                .gender(GENDER)
                .mbti(MBTI)
                .personalities(PERSONALITY_LIST)
                .goals(GOAL_LIST)
                .major(전공1)
                .user(유저1)
                .build();
        //when & then
        assertAll(
                () -> assertThat(profile.getNickname()).isEqualTo(NICKNAME),
                () -> assertThat(profile.getStudentId()).isEqualTo(STUDENT_ID),
                () -> assertThat(profile.getAge()).isEqualTo(AGE),
                () -> assertThat(profile.getImage()).isEqualTo(이미지1),
                () -> assertThat(profile.getGender()).isEqualTo(GENDER),
                () -> assertThat(profile.getMbti()).isEqualTo(MBTI),
                () -> assertThat(profile.getPersonalities()).isEqualTo(PERSONALITY_LIST),
                () -> assertThat(profile.getGoals()).isEqualTo(GOAL_LIST),
                () -> assertThat(profile.getMajor()).isEqualTo(전공1),
                () -> assertThat(profile.getUser()).isEqualTo(유저1)
        );
    }

    @Test
    void 프로필의_전공을_변경할_수_있다() {
        //given
        Profile profile = Profile.builder()
                .nickname(NICKNAME)
                .studentId(STUDENT_ID)
                .age(AGE)
                .image(이미지1)
                .gender(GENDER)
                .mbti(MBTI)
                .personalities(PERSONALITY_LIST)
                .goals(GOAL_LIST)
                .major(전공1)
                .user(유저1)
                .build();
        //when
        profile.updateMajor(전공2);
        //then
        assertThat(profile.getMajor()).isEqualTo(전공2);
    }

    @Test
    void 프로필의_필드를_변경할_수_있다() {
        //given
        Profile profile = Profile.builder()
                .nickname(NICKNAME)
                .studentId(STUDENT_ID)
                .age(AGE)
                .image(이미지1)
                .gender(GENDER)
                .mbti(MBTI)
                .personalities(PERSONALITY_LIST)
                .goals(GOAL_LIST)
                .major(전공1)
                .user(유저1)
                .build();

        ProfileRequest profileRequest = ProfileRequest.builder()
                .nickname("변경된 닉네임")
                .studentId(201814101)
                .age(24)
                .personalities(List.of(Personality.BRAVERY, Personality.BRIGHT, Personality.SILENT))
                .mbti(com.capstone.wanf.profile.domain.entity.MBTI.INFJ)
                .goals(List.of(Goal.GRADUATE, Goal.AREA_OF_INTEREST, Goal.TOP_OF_THE_MAJOR))
                .gender(Gender.FEMALE)
                .build();
        //when
        profile.updateField(profileRequest);
        //then
        assertAll(
                () -> assertThat(profile.getNickname()).isEqualTo(profileRequest.nickname()),
                () -> assertThat(profile.getStudentId()).isEqualTo(profileRequest.studentId()),
                () -> assertThat(profile.getAge()).isEqualTo(profileRequest.age()),
                () -> assertThat(profile.getPersonalities()).isEqualTo(profileRequest.personalities()),
                () -> assertThat(profile.getMbti()).isEqualTo(profileRequest.mbti()),
                () -> assertThat(profile.getGoals()).isEqualTo(profileRequest.goals()),
                () -> assertThat(profile.getGender()).isEqualTo(profileRequest.gender())
        );
    }

    @Test
    void 프로필을_DTO에_담는다() {
        //given
        Profile profile = Profile.builder()
                .nickname(NICKNAME)
                .studentId(STUDENT_ID)
                .age(AGE)
                .image(이미지1)
                .gender(GENDER)
                .mbti(MBTI)
                .personalities(PERSONALITY_LIST)
                .goals(GOAL_LIST)
                .major(전공1)
                .user(유저1)
                .build();

        //when
        ProfileResponse dto = ProfileResponse.of(profile);

        //then
        assertAll(
                () -> assertThat(dto.nickname()).isEqualTo(profile.getNickname()),
                () -> assertThat(dto.studentId()).isEqualTo(profile.getStudentId()),
                () -> assertThat(dto.age()).isEqualTo(profile.getAge()),
                () -> assertThat(dto.personalities()).isEqualTo(profile.getPersonalities().stream().collect(Collectors.toMap(Personality::name, Personality::getDetail))),
                () -> assertThat(dto.image()).isEqualTo(ImageResponse.of(profile.getImage())),
                () -> assertThat(dto.major().getName()).isEqualTo(profile.getMajor().getName()),
                () -> assertThat(dto.mbti()).isEqualTo(profile.getMbti()),
                () -> assertThat(dto.goals()).isEqualTo(profile.getGoals().stream().collect(Collectors.toMap(Goal::name, Goal::getDetail))),
                () -> assertThat(dto.gender()).isEqualTo(Map.of(profile.getGender().name(), profile.getGender().getGender()))
        );
    }
}