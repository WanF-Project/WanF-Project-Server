package com.capstone.wanf.message.converter;

import com.capstone.wanf.message.domain.entity.Message;
import com.capstone.wanf.message.domain.entity.MessageUtils;
import com.capstone.wanf.message.dto.response.MessageResponse;
import com.capstone.wanf.profile.domain.entity.Profile;
import com.capstone.wanf.profile.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessageConverter{
    private final ProfileService profileService;

    public Message convertMessage(MessageUtils messageUtils) {
        Profile receiverProfile = profileService.findById(messageUtils.receiverId());

        Message message = Message.builder()
                .receiver(receiverProfile.getUser())
                .sender(messageUtils.sender())
                .content(messageUtils.content())
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
