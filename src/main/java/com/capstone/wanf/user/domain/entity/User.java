package com.capstone.wanf.user.domain.entity;

import com.capstone.wanf.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.hibernate.annotations.BatchSize;

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
    private String email;       // Principal

    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[0-9]).{8,}$", message = "비밀번호는 영문과 숫자를 섞어서 8자 이상 입력해야 합니다.")
    @Column(name = "user_password")
    private String userPassword;        // Credential

    @Column(name = "verification_code", nullable = false)
    private String verificationCode;

    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    @BatchSize(size = 3)
    private List<Role> role = new ArrayList<>(); // 사용자 권한

    public void updateUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public void updateVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public void addRole(Role role) {
        this.role.add(role);
    }
}
