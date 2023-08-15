package com.capstone.wanf.message.converter;

import com.capstone.wanf.message.domain.entity.Message;
import com.capstone.wanf.message.domain.entity.KafkaMessage;
import com.capstone.wanf.message.dto.response.MessageResponse;
import com.capstone.wanf.profile.domain.entity.Profile;
import com.capstone.wanf.profile.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessageConverter{
    private final ProfileService profileService;

    public Message convertMessage(KafkaMessage kafkaMessage) {
        Profile receiverProfile = profileService.findById(kafkaMessage.receiverId());

        Profile senderProfile = profileService.findByUser(kafkaMessage.sender());

        Message message = Message.builder()
                .receiverProfile(receiverProfile)
                .senderProfile(senderProfile)
                .content(kafkaMessage.content())
                .build();

        return message;
    }

    public MessageResponse convertMessageResponse(Message message) {
        MessageResponse messageResponse = MessageResponse.builder()
                .senderProfileId(message.getSenderProfile().getId())
                .content(message.getContent())
                .createDate(message.getCreatedDate())
                .modifiedDate(message.getModifiedDate())
                .build();

        return messageResponse;
    }
}
