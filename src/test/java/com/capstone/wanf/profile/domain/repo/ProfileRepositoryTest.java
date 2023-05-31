package com.capstone.wanf.profile.domain.repo;

import com.capstone.wanf.course.domain.entity.Major;
import com.capstone.wanf.course.domain.repo.MajorRepository;
import com.capstone.wanf.profile.domain.entity.Profile;
import com.capstone.wanf.user.domain.entity.User;
import com.capstone.wanf.user.domain.repo.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static com.capstone.wanf.fixture.DomainFixture.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ProfileRepositoryTest {
    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MajorRepository majorRepository;

    @Test
    void 프로필_ID로_프로필을_조회할_수_있다(){
        //given
        final User user = userRepository.save(유저1);

        final Major major = majorRepository.save(전공1);

        final Profile savedProfile = profileRepository.save(프로필1.builder()
                .major(major)
                .user(user)
                .build());
        //when
        Profile profile = profileRepository.findById(savedProfile.getId()).orElseThrow();
        //then
        Assertions.assertThat(profile).isEqualTo(savedProfile);
    }

    @Test
    void 유저로_프로필을_조회할_수_있다(){
        //given
        final User user = userRepository.save(유저1);

        final Major major = majorRepository.save(전공1);

        final Profile savedProfile = profileRepository.save(프로필1.builder()
                .major(major)
                .user(user)
                .build());
        //when
        Profile profile = profileRepository.findByUser(user).orElseThrow();
        //then
        Assertions.assertThat(profile).isEqualTo(savedProfile);
    }

}