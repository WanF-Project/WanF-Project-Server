package com.capstone.wanf.profile.domain.repo;

import com.capstone.wanf.course.domain.entity.Major;
import com.capstone.wanf.course.domain.repo.MajorRepository;
import com.capstone.wanf.fixture.DomainFixture;
import com.capstone.wanf.profile.domain.entity.Profile;
import com.capstone.wanf.user.domain.entity.User;
import com.capstone.wanf.user.domain.repo.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ProfileRepositoryTest {
    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MajorRepository majorRepository;

    private User 유저1;

    private Major 전공1;

    private Profile 프로필1;
    @BeforeEach
    void setUp() {
        유저1 = userRepository.save(DomainFixture.유저2);

        전공1 = majorRepository.save(DomainFixture.전공1);

        프로필1 = profileRepository.save(Profile.builder()
                .major(전공1)
                .user(유저1)
                .build());
    }

    @Test
    void  프로필_ID로_프로필을_조회할_수_있다(){
        //when
        Profile profile = profileRepository.findById(프로필1.getId()).orElseThrow();
        //then
        Assertions.assertThat(profile).isEqualTo(프로필1);
    }

    @Test
    void 유저로_프로필을_조회할_수_있다(){
        //when
        Profile profile = profileRepository.findByUser(유저1).orElseThrow();
        //then
        Assertions.assertThat(profile).isEqualTo(프로필1);
    }

}