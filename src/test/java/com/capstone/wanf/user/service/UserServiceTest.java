package com.capstone.wanf.user.service;

import com.capstone.wanf.error.exception.RestApiException;
import com.capstone.wanf.user.domain.entity.Role;
import com.capstone.wanf.user.domain.entity.User;
import com.capstone.wanf.user.domain.repo.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static com.capstone.wanf.fixture.DomainFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Mock
    private BCryptPasswordEncoder encoder;

    @Test
    @DisplayName("이메일로 유저를 조회한다")
    void findByEmailSuccess() {
        // given
        given(userRepository.findByEmail(any(String.class))).willReturn(Optional.of(유저2));

        //when
        User user = userService.findByEmail(유저2.getEmail());

        //then
        assertThat(user).isEqualTo(유저2);
    }

    @Test
    @DisplayName("이메일 조회 결과가 null이면 예외가 발생한다")
    void findByEmailThrowException() {
        // given
        given(userRepository.findByEmail(any(String.class))).willReturn(Optional.empty());

        //when & then
        assertThatThrownBy(() -> userService.findByEmail("test@email.com"))
                .isInstanceOf(RestApiException.class);
    }

    @Test
    @DisplayName("인증번호가 유저 인증번호와 일치한다")
    void verifyVerificationCodeSuccess() {
        //given
        given(userRepository.findByEmail(any(String.class))).willReturn(Optional.of(유저2));

        // when
        User user = userService.verifyVerificationCode(유저2.getEmail(), 유저2.getVerificationCode());

        //then
        assertThat(user).isEqualTo(유저2);
    }

    @Test
    @DisplayName("인증번호 불일치 시 예외가 발생한다")
    void verifyVerificationCodeThrowException() {
        //given
        given(userRepository.findByEmail(any(String.class))).willReturn(Optional.of(유저2));

        // when & then
        assertThatThrownBy(() -> userService.verifyVerificationCode(유저2.getEmail(), "1234"))
                .isInstanceOf(RestApiException.class);
    }


    @Test
    @DisplayName("새로운 이메일인 경우 유저를 저장한다")
    void saveNewUser() {
        // when
        userService.createUser("test@email.com", "1234");

        // then
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("인증번호 재발급 시 유저의 인증번호를 갱신한다")
    void updateVerificationCode() {
        // given
        given(userRepository.findByEmail(any(String.class))).willReturn(Optional.of(유저2));

        // when
        userService.updateVerificationCode(유저2.getEmail(), "1234");

        // then
        assertThat(유저2.getVerificationCode()).isEqualTo("1234");
    }

    @Test
    @DisplayName("사용자의 비밀번호가 not null이 된다")
    void updateUserPasswordSuccess() {
        // given
        given(userRepository.findByEmail(any(String.class))).willReturn(Optional.of(유저2));

        given(encoder.encode(any(String.class))).willReturn(유저2.getUserPassword());

        // when
        User user = userService.updateUserPassword(회원가입_요청1);

        assertThat(user.getUserPassword()).isEqualTo(유저2.getUserPassword());
    }

    @Test
    @DisplayName("사용자에게 관리자 권한을 부여한다")
    void grantAdminRoleSuccess() {
        // given
        User user = 유저2;

        // when
        userService.grantAdminRole(유저2);

        // then
        assertThat(user.getRole()).isEqualTo(Role.ADMIN);
    }

    @Nested
    class isDuplicateUserTest {
        @Test
        @DisplayName("기존 유저이고 비밀번호가 not null일 때 예외가 발생한다")
        void existingUserThrowException() {
            // given
            given(userRepository.findByEmail(any(String.class))).willReturn(Optional.of(유저2));

            // when & then
            assertThatThrownBy(() -> userService.isDuplicateUser(유저2.getEmail()))
                    .isInstanceOf(RestApiException.class);
        }

        @Test
        @DisplayName("기존 유저이고 비밀번호가 null일 때 true를 반환한다")
        void existingUserReturnsTrue() {
            // given
            given(userRepository.findByEmail(any(String.class))).willReturn(Optional.of(유저3));

            // when
            Boolean isUser = userService.isDuplicateUser(유저3.getEmail());

            // then
            assertThat(isUser).isTrue();
        }

        @Test
        @DisplayName("새로운 유저일 때 false 반환한다")
        void newUserReturnsFalse() {
            // when
            Boolean isUser = userService.isDuplicateUser(any(String.class));

            // then
            assertThat(isUser).isFalse();
        }
    }
}


