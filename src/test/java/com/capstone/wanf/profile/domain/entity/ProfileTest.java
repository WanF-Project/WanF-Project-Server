package com.capstone.wanf.profile.domain.entity;

import com.capstone.wanf.course.domain.entity.Major;
import com.capstone.wanf.profile.dto.request.ProfileRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.capstone.wanf.fixture.DomainFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class ProfileTest {
    private static final String NICKNAME = "닉네임";

    private static final int STUDENT_ID = 201814100;

    private static final int AGE = 23;

    private static final String CONTACT = "010-1234-5678";

    private static final ProfileImage PROFILE_IMAGE = ProfileImage.BEAR;

    private static final Gender GENDER = Gender.MALE;

    private static final MBTI MBTI = com.capstone.wanf.profile.domain.entity.MBTI.ENFJ;

    private static final List<Personality> PERSONALITY_LIST = List.of(Personality.BRAVERY, Personality.BRIGHT);

    private static List<Goal> GOAL_LIST = List.of(Goal.GRADUATE, Goal.AREA_OF_INTEREST);

    @Test
    void 프로필을_생성할_수_있다(){
        //when & then
        assertThatCode(() -> Profile.builder()
                        .nickname(NICKNAME)
                        .studentId(STUDENT_ID)
                        .age(AGE)
                        .contact(CONTACT)
                        .profileImage(PROFILE_IMAGE)
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
    void 프로필에_저장된_정보를_조회할_수_있다(){
        //given
        Profile profile = Profile.builder()
                .nickname(NICKNAME)
                .studentId(STUDENT_ID)
                .age(AGE)
                .contact(CONTACT)
                .profileImage(PROFILE_IMAGE)
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
                () -> assertThat(profile.getContact()).isEqualTo(CONTACT),
                () -> assertThat(profile.getProfileImage()).isEqualTo(PROFILE_IMAGE),
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
                .contact(CONTACT)
                .profileImage(PROFILE_IMAGE)
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
    void 프로필의_필드를_변경할_수_있다(){
        //given
        Profile profile = Profile.builder()
                .nickname(NICKNAME)
                .studentId(STUDENT_ID)
                .age(AGE)
                .contact(CONTACT)
                .profileImage(PROFILE_IMAGE)
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
                .contact("010-1234-5679")
                .personalities(List.of(Personality.BRAVERY, Personality.BRIGHT, Personality.SILENT))
                .profileImage(ProfileImage.CAT)
                .mbti(com.capstone.wanf.profile.domain.entity.MBTI.INFJ)
                .goals(List.of(Goal.GRADUATE, Goal.AREA_OF_INTEREST, Goal.TOP_OF_THE_MAJOR))
                .gender(Gender.FEMALE)
                .build();
        //when
        profile.updateField(profileRequest);
        //then
        assertAll(
                () -> assertThat(profile.getNickname()).isEqualTo(profileRequest.getNickname()),
                () -> assertThat(profile.getStudentId()).isEqualTo(profileRequest.getStudentId()),
                () -> assertThat(profile.getAge()).isEqualTo(profileRequest.getAge()),
                () -> assertThat(profile.getContact()).isEqualTo(profileRequest.getContact()),
                () -> assertThat(profile.getPersonalities()).isEqualTo(profileRequest.getPersonalities()),
                () -> assertThat(profile.getProfileImage()).isEqualTo(profileRequest.getProfileImage()),
                () -> assertThat(profile.getMbti()).isEqualTo(profileRequest.getMbti()),
                () -> assertThat(profile.getGoals()).isEqualTo(profileRequest.getGoals()),
                () -> assertThat(profile.getGender()).isEqualTo(profileRequest.getGender())
        );
    }

}