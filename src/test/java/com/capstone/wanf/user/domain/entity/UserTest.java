package com.capstone.wanf.user.domain.entity;

import com.capstone.wanf.user.dto.response.UserResponse;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class UserTest {
    private static final String EMAIL = "이메일@wanf.com";

    private static final String PASSWORD = "비밀번호";

    private static final String VERIFICATION_CODE = "인증번호";

    private static final List<String> FCM_TOKEN_LIST = List.of("FCM_TOKEN1", "FCM_TOKEN2");

    @Test
    void 유저를_생성할_수_있다(){
        //given & when
        User 유저 = User.builder()
                .id(1L)
                .email(EMAIL)
                .userPassword(PASSWORD)
                .verificationCode(VERIFICATION_CODE)
                .fcmTokens(FCM_TOKEN_LIST)
                .role(Role.USER)
                .build();
        //then
        assertAll(
                () -> assertEquals(1L, 유저.getId()),
                () -> assertEquals(EMAIL, 유저.getEmail()),
                () -> assertEquals(PASSWORD, 유저.getUserPassword()),
                () -> assertEquals(VERIFICATION_CODE, 유저.getVerificationCode()),
                () -> assertEquals(FCM_TOKEN_LIST, 유저.getFcmTokens()),
                () -> assertEquals(Role.USER, 유저.getRole())
        );
    }

    @Test
    void 유저의_비밀번호를_변경할_수_있다(){
        //given
        User 유저 = User.builder()
                .id(1L)
                .email(EMAIL)
                .userPassword(PASSWORD)
                .verificationCode(VERIFICATION_CODE)
                .fcmTokens(FCM_TOKEN_LIST)
                .role(Role.USER)
                .build();
        //when
        유저.updateUserPassword("변경된 비밀번호");
        //then
        assertThat(유저.getUserPassword()).isEqualTo("변경된 비밀번호");
    }

    @Test
    void 윺저_인증번호를_업데이트한다(){
        //given
        User 유저 = User.builder()
                .id(1L)
                .email(EMAIL)
                .userPassword(PASSWORD)
                .verificationCode(VERIFICATION_CODE)
                .fcmTokens(FCM_TOKEN_LIST)
                .role(Role.USER)
                .build();
        //when
        유저.updateVerificationCode("변경된 인증번호");
        //then
        assertThat(유저.getVerificationCode()).isEqualTo("변경된 인증번호");
    }

    @Test
    void 유저의_권한을_업데이트한다(){
        //given
        User 유저 = User.builder()
                .id(1L)
                .email(EMAIL)
                .userPassword(PASSWORD)
                .verificationCode(VERIFICATION_CODE)
                .fcmTokens(FCM_TOKEN_LIST)
                .role(Role.USER)
                .build();
        //when
        유저.updateRole(Role.ADMIN);
        //then
        assertThat(유저.getRole()).isEqualTo(Role.ADMIN);
    }

    @Test
    void 유저를_DTO에_담는다(){
        //given
        User 유저 = User.builder()
                .id(1L)
                .email(EMAIL)
                .userPassword(PASSWORD)
                .verificationCode(VERIFICATION_CODE)
                .fcmTokens(FCM_TOKEN_LIST)
                .role(Role.USER)
                .build();
        //when
        UserResponse 유저DTO = UserResponse.of(유저);
        //then
        assertAll(
                () -> assertThat(유저DTO.id()).isEqualTo(1L),
                () -> assertThat(유저DTO.email()).isEqualTo(EMAIL),
                () -> assertThat(유저DTO.role()).isEqualTo(Role.USER));
    }
}