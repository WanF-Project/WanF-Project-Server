package com.capstone.wanf.profile.service;

import com.capstone.wanf.course.service.MajorService;
import com.capstone.wanf.error.exception.RestApiException;
import com.capstone.wanf.profile.domain.entity.Goal;
import com.capstone.wanf.profile.domain.entity.Personality;
import com.capstone.wanf.profile.domain.entity.Profile;
import com.capstone.wanf.profile.domain.repo.ProfileRepository;
import com.capstone.wanf.storage.service.S3Service;
import com.capstone.wanf.user.domain.entity.User;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;
import java.util.Optional;

import static com.capstone.wanf.fixture.DomainFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class ProfileServiceTest {
    @InjectMocks
    private ProfileService profileService;

    @Mock
    private ProfileRepository profileRepository;

    @Mock
    private MajorService majorService;

    @Mock
    private S3Service s3Service;

    @Test
    void 유저로_프로필을_조회한다() {
        //given
        given(profileRepository.findByUser(any(User.class))).willReturn(Optional.of(프로필2));
        //when
        Profile profile = profileService.findByUser(유저1);
        //then
        assertThat(profile.getUser()).isEqualTo(프로필2.getUser());
    }

    @Test
    void 유저로_프로필을_조회시_프로필이_없으면_예외를_던진다() {
        //given
        given(profileRepository.findByUser(any(User.class))).willReturn(Optional.empty());
        //when & then
        assertThatThrownBy(() -> profileService.findByUser(유저1))
                .isInstanceOf(RestApiException.class);
    }

    @Test
    void ID로_프로필을_조회한다() {
        //given
        given(profileRepository.findById(anyLong())).willReturn(Optional.of(프로필1));
        //when
        Profile profile = profileService.findById(1L);
        //then
        assertThat(profile).isEqualTo(프로필1);
    }

    @Test
    void ID로_프로필을_조회시_프로필이_없으면_예외를_던진다() {
        //given
        given(profileRepository.findById(anyLong())).willReturn(Optional.empty());
        //when & then
        assertThatThrownBy(() -> profileService.findById(1L))
                .isInstanceOf(RestApiException.class);
    }

//    @Test
//    void 기본_프로필을_생성한다() {
//        //given
//        final Profile defaultProfile = Profile.builder()
//                .user(유저1)
//                .build();
//
//        given(profileRepository.save(any(Profile.class))).willReturn(defaultProfile);
//        //when
//        Profile profile = profileService.defaultSave(유저1);
//        //then
//        assertThat(profile.getUser()).isEqualTo(유저1);
//    }

    @Nested
    class 프로필을_수정한다 {
        @Test
        void 프로필_수정시_사용자의_프로필이_존재하지_않으면_예외를_던진다() {
            //given
            given(profileRepository.findByUser(any(User.class))).willReturn(Optional.empty());
            //when & then
            assertThatThrownBy(() -> profileService.update(유저1, 프로필_이미지_수정1))
                    .isInstanceOf(RestApiException.class);
        }

        @Test
        void 프로필의_전공만을_수정한다() {
            //given
            given(profileRepository.findByUser(any(User.class))).willReturn(Optional.of(프로필3));

            given(majorService.findById(anyLong())).willReturn(전공1);
            //when
            Profile profile = profileService.update(유저1, 프로필_이미지_수정1);
            //then
            assertThat(profile.getMajor()).isEqualTo(전공1);
        }

        @Test
        void 프로필의_이미지만을_수정한다() {
            //given
            given(profileRepository.findByUser(any(User.class))).willReturn(Optional.of(프로필3));

            given(s3Service.findById(anyLong())).willReturn(이미지1);
            //when
            Profile profile = profileService.update(유저1, 프로필_이미지_수정3);
            //then
            assertThat(profile.getImage()).isEqualTo(이미지1);
        }

        @Test
        void 프로필의_필드만을_수정한다() {
            //given
            given(profileRepository.findByUser(any(User.class))).willReturn(Optional.of(프로필3));
            //when
            Profile profile = profileService.update(유저1, 프로필_이미지_수정2);
            //then
            assertAll(
                    () -> assertThat(profile.getContact()).isEqualTo(프로필_수정2.contact()),
                    () -> assertThat(profile.getAge()).isEqualTo(프로필_수정2.age()),
                    () -> assertThat(profile.getNickname()).isEqualTo(프로필_수정2.nickname()),
                    () -> assertThat(profile.getStudentId()).isEqualTo(프로필_수정2.studentId()),
                    () -> assertThat(profile.getGender()).isEqualTo(프로필_수정2.gender()),
                    () -> assertThat(profile.getMbti()).isEqualTo(프로필_수정2.mbti()),
                    () -> assertThat(profile.getGoals()).isEqualTo(프로필_수정2.goals()),
                    () -> assertThat(profile.getPersonalities()).isEqualTo(프로필_수정2.personalities())
            );
        }

        @Test
        void 프로필의_전공과_이미지와_필드를_수정한다() {
            //given
            given(profileRepository.findByUser(any(User.class))).willReturn(Optional.of(프로필3));

            given(majorService.findById(anyLong())).willReturn(전공1);

            given(s3Service.findById(anyLong())).willReturn(이미지1);
            //when
            Profile profile = profileService.update(유저1, 프로필_이미지_수정3);
            //then
            assertAll(
                    () -> assertThat(profile.getMajor()).isEqualTo(전공1),
                    () -> assertThat(profile.getContact()).isEqualTo(프로필_수정3.contact()),
                    () -> assertThat(profile.getAge()).isEqualTo(프로필_수정3.age()),
                    () -> assertThat(profile.getNickname()).isEqualTo(프로필_수정3.nickname()),
                    () -> assertThat(profile.getStudentId()).isEqualTo(프로필_수정3.studentId()),
                    () -> assertThat(profile.getGender()).isEqualTo(프로필_수정3.gender()),
                    () -> assertThat(profile.getMbti()).isEqualTo(프로필_수정3.mbti()),
                    () -> assertThat(profile.getGoals()).isEqualTo(프로필_수정3.goals()),
                    () -> assertThat(profile.getPersonalities()).isEqualTo(프로필_수정3.personalities()),
                    () -> assertThat(profile.getMajor()).isEqualTo(전공1),
                    () -> assertThat(profile.getImage()).isEqualTo(이미지1)
            );
        }
    }

    @Test
    void 어떤_성격이_있는지_조회한다() {
        //given & when
        Map<String, String> personalities = profileService.getPersonalities();
        //then
        Personality[] values = Personality.values();

        for (Personality value : values) {
            assertThat(personalities.get(value.name())).isEqualTo(value.getDetail());
        }
    }

    @Test
    void 어떤_목표가_있는지_조회한다() {
        //given & when
        Map<String, String> goals = profileService.getGoals();
        //then
        Goal[] values = Goal.values();

        for (Goal value : values) {
            assertThat(goals.get(value.name())).isEqualTo(value.getDetail());
        }
    }

    @Test
    void 프로필을_저장한다() {
        //given
        given(majorService.findById(anyLong())).willReturn(전공1);

        given(profileRepository.save(any(Profile.class))).willReturn(프로필1);
        //when
        Profile profile = profileService.save(프로필_이미지_수정3, 유저1);
        //then
        assertThat(profile).isEqualTo(프로필1);
    }

    @Test
    void 프로필_요청의_imageId가_null이면_기본_이미지를_저장한다() {
        //given
        given(profileRepository.save(any(Profile.class))).willReturn(프로필1);
        //when
        Profile profile = profileService.save(프로필_이미지_수정2, 유저2);
        //then
        assertThat(profile.getImage()).isEqualTo(이미지1);
    }
}