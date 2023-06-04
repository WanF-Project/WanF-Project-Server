package com.capstone.wanf.fixture;

import com.capstone.wanf.course.domain.entity.Course;
import com.capstone.wanf.course.domain.entity.Major;
import com.capstone.wanf.profile.domain.entity.*;
import com.capstone.wanf.profile.dto.request.ProfileRequest;
import com.capstone.wanf.user.domain.entity.User;
import com.capstone.wanf.course.dto.request.CourseRequest;

import java.util.List;

public class DomainFixture {
    public static final Major 전공1 = Major.builder()
            .name("전공1")
            .build();

    public static final Major 전공2 = Major.builder()
            .name("전공2")
            .build();

    public static final Course 수업1 = Course.builder()
            .name("수업1")
            .category("카테고리1")
            .courseTime("수업시간1")
            .courseId("과목코드1")
            .professor("교수1")
            .build();

    public static final User 유저1 = User.builder()
            .email("royqwe16@gmail.com")
            .userPassword("1234@@qwer")
            .verificationCode("1234")
            .build();

    public static final Profile 프로필1 = Profile.builder()
            .nickname("닉네임1")
            .studentId(12345678)
            .age(1)
            .contact("연락처1")
            .profileImage(ProfileImage.BEAR)
            .gender(Gender.MALE)
            .mbti(MBTI.INFJ)
            .personalities(List.of(Personality.BRAVERY))
            .goals(List.of(Goal.GRADUATE))
            .build();

    public static final Profile 프로필2 = Profile.builder()
            .nickname("닉네임2")
            .studentId(12345678)
            .age(1)
            .contact("연락처2")
            .profileImage(ProfileImage.BEAR)
            .gender(Gender.MALE)
            .mbti(MBTI.INFJ)
            .personalities(List.of(Personality.BRAVERY))
            .goals(List.of(Goal.GRADUATE))
            .user(유저1)
            .major(전공1)
            .build();

    public static final Profile 프로필3 = Profile.builder()
            .nickname("닉네임3")
            .studentId(12345678)
            .age(1)
            .contact("연락처3")
            .profileImage(ProfileImage.BEAR)
            .gender(Gender.MALE)
            .mbti(MBTI.INFJ)
            .personalities(List.of(Personality.BRAVERY))
            .goals(List.of(Goal.GRADUATE))
            .user(유저1)
            .major(전공2)
            .build();

    public static final ProfileRequest 프로필_수정1 = ProfileRequest.builder()
            .majorId(1L)
            .age(0)
            .studentId(0)
            .build();

    public static final ProfileRequest 프로필_수정2 = ProfileRequest.builder()
            .goals(List.of(Goal.GRADUATE))
            .gender(Gender.MALE)
            .contact("연락처2")
            .profileImage(ProfileImage.BEAR)
            .studentId(12345678)
            .age(1)
            .mbti(MBTI.INFJ)
            .personalities(List.of(Personality.BRAVERY))
            .nickname("닉네임2")
            .build();

    public static final ProfileRequest 프로필_수정3 = ProfileRequest.builder()
            .goals(List.of(Goal.GRADUATE))
            .gender(Gender.MALE)
            .contact("연락처3")
            .profileImage(ProfileImage.BEAR)
            .studentId(12345678)
            .age(1)
            .mbti(MBTI.INFJ)
            .personalities(List.of(Personality.BRAVERY))
            .nickname("닉네임3")
            .majorId(1L)
            .build();



    public static final CourseRequest 수업_요청1 = new CourseRequest("수업명", "카테고리", "수업시간", "과목코드", "교수");


}
