package com.capstone.wanf.user.service;

import com.capstone.wanf.user.domain.entity.User;
import com.capstone.wanf.user.domain.repo.UserRepository;
import com.capstone.wanf.user.dto.request.CodeRequest;
import com.capstone.wanf.user.dto.request.EmailRequest;
import com.capstone.wanf.user.dto.request.UserRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Random;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final JavaMailSender mailSender;

    public String generateVerificationCode() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder verificationCode = new StringBuilder();
        Random random = new Random();
        int codeLength = 6;

        for (int i = 0; i < codeLength; i++) {
            verificationCode.append(characters.charAt(random.nextInt(characters.length())));
        }

        return verificationCode.toString();
    }

    public void sendVerificationCode(EmailRequest emailRequest, String verificationCode) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(emailRequest.getEmail());
        message.setSubject("회원가입 인증번호");
        message.setText("인증번호는 [" + verificationCode + "] 입니다.");
        mailSender.send(message);

        save(emailRequest.getEmail(), verificationCode);
    }

    @Transactional
    public void save(String email, String verificationCode) {
        User user = User.builder()
                .email(email)
                .verificationCode(verificationCode)
                .build();
        userRepository.save(user);
    }

    @Transactional
    public void verify(CodeRequest codeRequest) {
        // 이메일과 인증번호를 사용하여 사용자 정보 검증
        User user = userRepository.findByEmailAndVerificationCode(codeRequest.getEmail(), codeRequest.getVerificationCode())
                .orElseThrow(()->new ResponseStatusException(HttpStatus.BAD_REQUEST, "인증번호가 올바르지 않습니다."));
    }

    @Transactional
    public void update(UserRequest userRequest) {
        User user = userRepository.findByEmail(userRequest.getEmail())
                .orElseThrow(()->new ResponseStatusException(HttpStatus.BAD_REQUEST, "이메일이 잘못되었습니다."));

        // 비밀번호 저장
        user.update(userRequest.getUserPassword());
        userRepository.save(user);
    }
}
