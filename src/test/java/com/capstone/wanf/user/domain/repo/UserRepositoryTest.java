package com.capstone.wanf.user.domain.repo;

import com.capstone.wanf.fixture.DomainFixture;
import com.capstone.wanf.user.domain.entity.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    private User 유저1;

    @BeforeEach
    void setUp() {
        유저1 = userRepository.save(DomainFixture.유저2);
    }

    @Test
    @DisplayName("이메일로 유저를 조회한다")
    void findByEmailSuccess() {
        //when
        User user = userRepository.findByEmail(유저1.getEmail()).orElseThrow();
        //then
        Assertions.assertThat(user).isEqualTo(유저1);
    }
}
