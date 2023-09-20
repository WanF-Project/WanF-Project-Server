package com.capstone.wanf.message.service;

import com.capstone.wanf.error.errorcode.CommonErrorCode;
import com.capstone.wanf.error.exception.RestApiException;
import com.capstone.wanf.message.domain.entity.KafkaMessage;
import com.capstone.wanf.message.dto.request.MessageRequest;
import com.capstone.wanf.profile.domain.entity.Profile;
import com.capstone.wanf.profile.service.ProfileService;
import com.capstone.wanf.user.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import static com.capstone.wanf.error.errorcode.CommonErrorCode.*;

@Service
@RequiredArgsConstructor
public class MessageProducerService {
    private final KafkaTemplate<String, KafkaMessage> kafkaTemplate;

    private final ProfileService profileService;


    public boolean validateMessage(Long receiverProfileId, User sender) {
        Profile senderProfile = profileService.findByUser(sender);

        return senderProfile.getId().equals(receiverProfileId);
    }

    public void sendMessage(MessageRequest messageRequest, User sender) {
        if (validateMessage(messageRequest.receiverProfileId(),sender)) {
            throw new RestApiException(INVALID_MESSAGE);
        }

        kafkaTemplate.send("message", messageRequest.receiverProfileId().toString(), KafkaMessage.builder()
                .receiverProfileId(messageRequest.receiverProfileId())
                .content(messageRequest.content())
                .sender(sender)
                .build());
    }
}
