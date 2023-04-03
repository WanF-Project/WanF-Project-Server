package com.capstone.wanf.user.service;

import com.capstone.wanf.user.domain.entity.User;
import com.capstone.wanf.user.domain.repo.UserRepository;
import com.capstone.wanf.user.dto.request.CodeRequest;
import com.capstone.wanf.user.dto.request.UserRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public void save(String email, String verificationCode) {
        User user = User.builder()
                .email(email)
                .verificationCode(verificationCode)
                .build();
        userRepository.save(user);
    }

    @Transactional
    public void updateUserPassword(UserRequest userRequest) {
        User user = findByEmail(userRequest.getEmail());
        // 비밀번호 저장
        user.update(userRequest.getUserPassword());
        userRepository.save(user);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "유저 정보가 없습니다."));
    }

    public User findByEmailAndVerificationCode(CodeRequest codeRequest) {
        return userRepository.findByEmailAndVerificationCode(codeRequest.getEmail(), codeRequest.getVerificationCode())
                .orElseThrow(()->new ResponseStatusException(HttpStatus.BAD_REQUEST, "인증번호가 올바르지 않습니다."));
    }

    public void delete(User user) {
        userRepository.delete(user);
    }
}
