package com.capstone.wanf.profile.domain.repo;

import com.capstone.wanf.config.TestQueryDslConfig;
import com.capstone.wanf.course.domain.entity.Major;
import com.capstone.wanf.course.domain.repo.MajorRepository;
import com.capstone.wanf.fixture.DomainFixture;
import com.capstone.wanf.profile.domain.entity.Profile;
import com.capstone.wanf.profile.dto.response.ProfileResponse;
import com.capstone.wanf.user.domain.entity.User;
import com.capstone.wanf.user.domain.repo.UserRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(TestQueryDslConfig.class)
class ProfileRepositoryTest {
    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MajorRepository majorRepository;

    @Autowired
    private JPAQueryFactory queryFactory;

    private ProfileRepositorySupport profileRepositorySupport;

    private User 유저1;

    private Major 전공1;

    private Profile 프로필1;

    @BeforeEach
    void setUp() {
        profileRepositorySupport = new ProfileRepositorySupport(queryFactory);

        유저1 = userRepository.save(DomainFixture.유저2);

        전공1 = majorRepository.save(DomainFixture.전공1);

        프로필1 = profileRepository.save(Profile.builder()
                .major(전공1)
                .user(유저1)
                .build());
    }

    @Test
    void 프로필_ID로_프로필을_조회할_수_있다() {
        //when
        Profile profile = profileRepository.findById(프로필1.getId()).orElseThrow();
        //then
        assertThat(profile).isEqualTo(프로필1);
    }

    @Test
    void 유저로_프로필을_조회할_수_있다() {
        //when
        Profile profile = profileRepository.findByUser(유저1).orElseThrow();
        //then
        assertThat(profile).isEqualTo(프로필1);
    }

    @Nested
    class 랜덤_프로필_목록을_조회한다 {
        @Test
        void 디폴트_정렬_페이징() {
            //given
            profileRepository.findByUser(유저1).orElseThrow();

            Pageable pageable = PageRequest.of(0, 5);

            //when
            Slice<ProfileResponse> profileResponses = profileRepositorySupport.findByRandom(pageable, 유저1.getId());
            //then
            assertThat(profileResponses).hasSize(1);
        }

        @Test
        void DESC순으로_정렬된_페이징() {
            //given
            Sort.Order order = new Sort.Order(Sort.Direction.DESC, "ex");

            Sort sort = Sort.by(order);

            Pageable pageable = PageRequest.of(0, 5, sort);
            //when
            Slice<ProfileResponse> profileResponses = profileRepositorySupport.findByRandom(pageable, 프로필1.getId());
            //then
            assertThat(profileResponses).hasSize(1);
        }

        @Test
        void ASC순으로_정렬된_페이징() {
            //given
            Sort.Order order = new Sort.Order(Sort.Direction.ASC, "ex");

            Sort sort = Sort.by(order);

            Pageable pageable = PageRequest.of(0, 5, sort);

            //when
            Slice<ProfileResponse> profileResponses = profileRepositorySupport.findByRandom(pageable, 프로필1.getId());
            //then
            assertThat(profileResponses).hasSize(1);
        }
    }
}