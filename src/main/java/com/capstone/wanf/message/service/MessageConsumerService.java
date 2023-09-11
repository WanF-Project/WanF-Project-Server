package com.capstone.wanf.message.service;

import com.capstone.wanf.firebase.service.FCMService;
import com.capstone.wanf.message.converter.MessageConverter;
import com.capstone.wanf.message.domain.entity.KafkaMessage;
import com.capstone.wanf.message.domain.entity.Message;
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

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MessageConsumerService {
    private final MessageRepository messageRepository;

    private final MessageConverter messageConverter;

    private final MessageRepositorySupport messageRepositorySupport;

    private final ProfileService profileService;

    private final FCMService fcmService;

    @KafkaListener(topics = "message")
    @Transactional
    public void receive(KafkaMessage kafkaMessage) {
        fcmService.sendMessageNotification(kafkaMessage);

        Message message = messageConverter.convertMessage(kafkaMessage);

        messageRepository.save(message);
    }

    @Transactional(readOnly = true)
    public ReceiverMessageResponse getMessage(User receiver, Long senderProfileId) {
        Profile senderProfile = profileService.findById(senderProfileId);

        Profile receiverProfile = profileService.findByUser(receiver);

        List<MessageResponse> messages = messageRepositorySupport.findMessagesByReceiverAndSender(receiverProfile, senderProfile);

        return ReceiverMessageResponse.builder()
                .myProfileId(receiverProfile.getId())
                .messages(messages)
                .build();
    }

    public List<ProfileResponse> getSenders(User user) {
        Profile receiverProfile = profileService.findByUser(user);

        return messageRepositorySupport.findSenderByReceiver(receiverProfile).stream()
                .map(Profile::toResponse)
                .collect(Collectors.toList());
    }
}
