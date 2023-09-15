package com.capstone.wanf.user.service;

import com.capstone.wanf.error.exception.RestApiException;
import com.capstone.wanf.profile.service.ProfileService;
import com.capstone.wanf.user.domain.entity.Role;
import com.capstone.wanf.user.domain.entity.User;
import com.capstone.wanf.user.domain.repo.UserRepository;
import com.capstone.wanf.user.dto.request.UserRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.capstone.wanf.error.errorcode.CustomErrorCode.*;


@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

    private final BCryptPasswordEncoder encoder;

    private final ProfileService profileService;

    public boolean isDuplicateUser(String email) {
        Optional<User> optUser = userRepository.findByEmail(email);
        if (optUser.isPresent()) {
            User user = optUser.get();
            if (user.getUserPassword() != null) {
                throw new RestApiException(DUPLICATE_RESOURCE);
            }
            return true;
        }
        return false;
    }

    @Transactional
    public void updateVerificationCode(String email, String verificationCode) {
        User user = findByEmail(email);

        user.updateVerificationCode(verificationCode);

        userRepository.save(user);
    }

    @Transactional
    public void createUser(String email, String verificationCode) {
        User user = User.builder()
                .email(email)
                .verificationCode(verificationCode)
                .role(Role.USER)
                .build();

        userRepository.save(user);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RestApiException(USER_NOT_FOUND));
    }

    @Transactional
    public User updateUserPassword(UserRequest userRequest) {
        User user = findByEmail(userRequest.email());

        // 비밀번호 저장
        String encodedPassword = encoder.encode(userRequest.userPassword());
        user.updateUserPassword(encodedPassword);

        userRepository.save(user);

        return user;
    }

    public User verifyVerificationCode(String email, String verificationCode) {
        User user = findByEmail(email);

        if (user.getVerificationCode().equals(verificationCode)) return user;
        else throw new RestApiException(VERIFICATION_NOT_FOUND);
    }

    @Transactional
    public void grantAdminRole(User user) {
        user.updateRole(Role.ADMIN);

        userRepository.save(user);
    }

    @Transactional
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RestApiException(USER_NOT_FOUND));
    }

    @Transactional
    public void verifyAndUpdateFcmToken(UserRequest userRequest, String fcmToken) {
        User user = findByEmail(userRequest.email());

        List<String> fcmTokens = user.getFcmTokens();

        if (!fcmTokens.contains(fcmToken)) {
            if (fcmTokens.size() == 3) fcmTokens.remove(0);

            fcmTokens.add(fcmToken);

            userRepository.save(user);
        }
    }

    @Transactional
    public void removeFcmToken(User user, String fcmToken) {
        List<String> fcmTokens = user.getFcmTokens();

        fcmTokens.remove(fcmToken);

        userRepository.save(user);
    }
}
