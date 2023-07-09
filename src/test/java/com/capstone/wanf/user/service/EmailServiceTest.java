package com.capstone.wanf.user.service;

import com.capstone.wanf.error.exception.RestApiException;
import com.capstone.wanf.user.domain.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.time.LocalDateTime;

import static com.capstone.wanf.fixture.DomainFixture.이메일_요청1;
import static com.capstone.wanf.fixture.DomainFixture.인증번호_요청1;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class EmailServiceTest {
    @Mock
    private UserService userService;

    @InjectMocks
    private EmailService emailService;

    @Mock
    private JavaMailSender mailSender;

    @Nested
    @DisplayName("인증번호 만료 여부 검사")
    class verifyTest {
        @Test
        @DisplayName("기존 유저는 false 반환")
        void verifyExistingUserReturnsFalse() {
            // given
            given(userService.isDuplicateUser(any(String.class))).willReturn(false);

            // when
            boolean isSuccess = emailService.verify(인증번호_요청1);

            // then
            assertThat(isSuccess).isFalse();
        }

        @Test
        @DisplayName("생성 후 30분 이하")
        void unexpiredVerificationCodeTest() {
            // given
            // Jpa Auditing을 사용한 User 객체는 생성 시간, 수정 시간을 Builder로 직접 설정 불가능 -> Mock 객체를 사용해 기댓값을 직접 작성해 테스트
            User unexpiredUser = Mockito.mock(User.class);
            LocalDateTime modifiedDate = LocalDateTime.now().minusMinutes(29);
            given(unexpiredUser.getModifiedDate()).willReturn(modifiedDate);

            given(userService.verifyVerificationCode(any(String.class), any(String.class))).willReturn(unexpiredUser);

            given(userService.isDuplicateUser(any(String.class))).willReturn(true);

            // when
            boolean isSuccess = emailService.verify(인증번호_요청1);

            // then
            assertThat(isSuccess).isTrue();
        }

        @Test
        @DisplayName("생성 후 30분을 초과해 예외가 발생")
        void expiredVerificationCodeTest() {
            // given
            User unexpiredUser = Mockito.mock(User.class);
            LocalDateTime modifiedDate = LocalDateTime.now().minusMinutes(31);
            given(unexpiredUser.getModifiedDate()).willReturn(modifiedDate);

            given(userService.verifyVerificationCode(인증번호_요청1.email(), 인증번호_요청1.verificationCode())).willReturn(unexpiredUser);

            given(userService.isDuplicateUser(any(String.class))).willReturn(true);

            // when & then
            assertThatThrownBy(() -> emailService.verify(인증번호_요청1))
                    .isInstanceOf(RestApiException.class);
        }
    }

    @Test
    @DisplayName("재전송을 요청하면 인증번호를 갱신한다")
    void existingUserUpdateVerificationCode() {
        // given
        given(userService.isDuplicateUser(any(String.class))).willReturn(true);

        // when
        boolean isSuccess = emailService.sendVerificationCode(이메일_요청1, "1234");

        // then
        verify(userService).updateVerificationCode(이메일_요청1.email(), "1234");

        verify(mailSender).send(any(SimpleMailMessage.class));

        assertThat(isSuccess).isTrue();
    }
}