package com.capstone.wanf.message.service;

import com.capstone.wanf.message.converter.MessageConverter;
import com.capstone.wanf.message.domain.entity.Message;
import com.capstone.wanf.message.domain.entity.MessageUtils;
import com.capstone.wanf.message.domain.repo.MessageRepository;
import com.capstone.wanf.message.domain.repo.MessageRepositorySupport;
import com.capstone.wanf.message.dto.response.MessageResponse;
import com.capstone.wanf.message.dto.response.ReceiverMessageResponse;
import com.capstone.wanf.profile.domain.entity.Profile;
import com.capstone.wanf.profile.dto.response.ProfileResponse;
import com.capstone.wanf.profile.service.ProfileService;
import com.capstone.wanf.user.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ConsumerService {
    private final MessageRepository messageRepository;

    private final MessageConverter messageConverter;

    private final MessageRepositorySupport messageRepositorySupport;

    private final ProfileService profileService;

    @KafkaListener(topics = "message")
    @Transactional
    public void receive(MessageUtils messageUtils) {
        // TODO: 2023/07/18 FCM으로 알람보내기
        Message message = messageConverter.convertMessage(messageUtils);

        messageRepository.save(message);
    }

    @Transactional(readOnly = true)
    public ReceiverMessageResponse getMessage(User receiver,Long senderProfileId) {
        Profile senderProfile = profileService.findById(senderProfileId);

        List<MessageResponse> messages = messageRepository.findMessagesByReceiverAndSender(receiver, senderProfile.getUser())
                .stream()
                .map(messageConverter::convertMessageResponse)
                .sorted(Comparator.comparing(MessageResponse::modifiedDate).reversed())
                .collect(Collectors.toList());

        return ReceiverMessageResponse.builder()
                .senderProfile(senderProfile.toDTO())
                .messages(messages)
                .build();
    }

    public List<ProfileResponse> getSenders(User user) {
        return messageRepositorySupport.findSenderByReceiver(user).stream()
                .map(profileService::findByUser)
                .map(Profile::toDTO)
                .collect(Collectors.toList());
    }
}