package com.capstone.wanf.user.service;

import com.capstone.wanf.error.exception.RestApiException;
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

import static com.capstone.wanf.error.errorcode.CustomErrorCode.INVALID_VERIFICATION_CODE;

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

    public boolean sendVerificationCode(EmailRequest emailRequest, String verificationCode) {
        String email = emailRequest.email();

        if (userService.isDuplicateUser(email)) userService.updateVerificationCode(email, verificationCode);

        else userService.createUser(email, verificationCode);

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(email);

        message.setSubject("[From WanF] 이메일 인증 번호입니다.");

        message.setText("인증 번호는 [" + verificationCode + "] 입니다.");

        mailSender.send(message);

        return true;
    }

    @Transactional
    public boolean verify(CodeRequest codeRequest) {
        if (userService.isDuplicateUser(codeRequest.email())) {
            User user = userService.verifyVerificationCode(codeRequest.email(), codeRequest.verificationCode());

            LocalDateTime createdDate = user.getModifiedDate();

            Duration validDuration = Duration.ofMinutes(30);

            LocalDateTime validUntil = createdDate.plus(validDuration);

            if (LocalDateTime.now().isAfter(validUntil)) {
                throw new RestApiException(INVALID_VERIFICATION_CODE);
            }

            return true;
        }
        return false;
    }
}