package com.capstone.wanf.user.domain.entity;

import com.capstone.wanf.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Table(name = "user")
@Entity
public class User extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Email(message = "이메일 형식이 잘못되었습니다.")
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[0-9]).{8,}$", message = "비밀번호는 영문과 숫자를 섞어서 8자 이상 입력해야 합니다.")
    @Column(name = "user_password")
    private String userPassword;

    @Column(name = "verification_code", nullable = false)
    private String verificationCode;

    @Builder.Default
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> fcmTokens = new ArrayList<>(3);

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    public void updateUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public void updateVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public void updateRole(Role admin) {
        this.role = admin;
    }
}
