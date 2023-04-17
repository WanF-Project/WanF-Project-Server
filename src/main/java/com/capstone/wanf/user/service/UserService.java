package com.capstone.wanf.user.service;

import com.capstone.wanf.error.exception.RestApiException;
import com.capstone.wanf.user.domain.entity.Role;
import com.capstone.wanf.user.domain.entity.User;
import com.capstone.wanf.user.domain.repo.UserRepository;
import com.capstone.wanf.user.dto.request.UserRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.capstone.wanf.error.errorcode.CustomErrorCode.*;


@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

    private final BCryptPasswordEncoder encoder;

    public void saveOrUpdate(String email, String verificationCode) {
        findByEmail(email).ifPresentOrElse(
                existingUser -> {   // 이미 DB에 존재하는 이메일일 경우
                    if (existingUser.getUserPassword() != null) {
                        throw new RestApiException(DUPLICATE_RESOURCE);
                    }

                    existingUser.updateVerificationCode(verificationCode);

                    userRepository.save(existingUser);
                },
                () -> {             // 새로운 이메일인 경우
                    User newUser = User.builder()
                            .email(email)
                            .verificationCode(verificationCode)
                            .role(Role.USER)
                            .build();

                    userRepository.save(newUser);
                }
        );
    }

    @Transactional
    public void updateUserPassword(UserRequest userRequest) {
        User user = findByEmail(userRequest.getEmail())
                .orElseThrow(() -> new RestApiException(USER_NOT_FOUND));

        // 비밀번호 저장
        user.updateUserPassword(encoder.encode(userRequest.getUserPassword()));

        userRepository.save(user);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User findByEmailAndVerificationCode(String email, String verificationCode) {
        return userRepository.findByEmailAndVerificationCode(email, verificationCode)
                .orElseThrow(() -> new RestApiException(VERIFICATION_NOT_FOUND));
    }
}
