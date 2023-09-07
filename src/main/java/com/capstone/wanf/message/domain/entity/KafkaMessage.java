package com.capstone.wanf.message.domain.entity;

import com.capstone.wanf.user.domain.entity.User;
import lombok.Builder;

@Builder
public record KafkaMessage(
        User sender,
        Long receiverProfileId,
        String content
) {
}
