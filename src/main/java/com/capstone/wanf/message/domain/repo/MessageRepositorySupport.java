package com.capstone.wanf.message.domain.repo;

import com.capstone.wanf.message.converter.MessageConverter;
import com.capstone.wanf.message.domain.entity.QMessage;
import com.capstone.wanf.message.dto.response.MessageResponse;
import com.capstone.wanf.profile.domain.entity.Profile;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class MessageRepositorySupport {
    private final JPAQueryFactory jpaQueryFactory;

    private final MessageConverter messageConverter;

    private final QMessage message = QMessage.message;

    public List<Profile> findSenderByReceiver(Profile receiverProfile) {
        List<Profile> sendersProfile = jpaQueryFactory.select(message.senderProfile)
                .from(message)
                .where(message.receiverProfile.eq(receiverProfile))
                .orderBy(message.modifiedDate.desc())
                .distinct()
                .fetch();

        return sendersProfile;
    }

    public List<MessageResponse> findMessagesByReceiverAndSender(Profile receiverProfile, Profile senderProfile) {
        List<MessageResponse> messages = jpaQueryFactory.select(message)
                .from(message)
                .where(message.receiverProfile.eq(receiverProfile)
                        .and(message.senderProfile.eq(senderProfile))
                        .or(message.receiverProfile.eq(senderProfile)
                                .and(message.senderProfile.eq(receiverProfile))))
                .orderBy(message.modifiedDate.desc())
                .fetch()
                .stream()
                .map(messageConverter::convertMessageResponse)
                .collect(Collectors.toList());

        return messages;
    }
}
