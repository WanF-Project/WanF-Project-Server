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

        Message message = Message.builder()
                .receiver(receiverProfile.getUser())
                .sender(kafkaMessage.sender())
                .content(kafkaMessage.content())
                .build();

        return message;
    }

    public MessageResponse convertMessageResponse(Message message) {
        MessageResponse messageResponse = MessageResponse.builder()
                .content(message.getContent())
                .createDate(message.getCreatedDate())
                .modifiedDate(message.getModifiedDate())
                .build();

        return messageResponse;
    }
}
