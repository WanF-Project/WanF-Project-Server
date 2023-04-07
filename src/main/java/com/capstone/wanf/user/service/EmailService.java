package com.capstone.wanf.user.service;

import com.capstone.wanf.user.domain.entity.User;
import com.capstone.wanf.user.dto.request.CodeRequest;
import com.capstone.wanf.user.dto.request.EmailRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Random;

@RequiredArgsConstructor
@Service
public class EmailService {
    private final JavaMailSender mailSender;

    private final UserService userService;

    public String generateVerificationCode() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

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

        message.setSubject("[From WanF] 이메일 인증 번호입니다.");

        message.setText("인증 번호는 [" + verificationCode + "] 입니다.");

        userService.saveOrUpdate(emailRequest.getEmail(), verificationCode);

        mailSender.send(message);
    }

    @Transactional
    public void verify(CodeRequest codeRequest) {
        User user = userService.findByEmailAndVerificationCode(codeRequest.getEmail(), codeRequest.getVerificationCode());

        LocalDateTime createdDate = user.getModifiedDate(); // 인증번호 생성 시각

        // 인증번호 유효 시간
        Duration validDuration = Duration.ofMinutes(10);

        LocalDateTime validUntil = createdDate.plus(validDuration);

        if (LocalDateTime.now().isAfter(validUntil) && user.getUserPassword() == null) {
            throw new IllegalArgumentException("인증번호를 다시 발급받아주세요.");
        }
    }
}
