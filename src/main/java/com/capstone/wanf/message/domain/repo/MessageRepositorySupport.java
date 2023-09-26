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
import java.util.stream.Stream;

@Repository
@RequiredArgsConstructor
public class MessageRepositorySupport {
    private final JPAQueryFactory jpaQueryFactory;

    private final MessageConverter messageConverter;

    private final QMessage message = QMessage.message;

    public List<Profile> findInteractedProfiles(Profile myProfile) {
        List<Profile> profiles = jpaQueryFactory.select(message)
                .from(message)
                .where(message.receiverProfile.eq(myProfile).or(message.senderProfile.eq(myProfile)))
                .orderBy(message.modifiedDate.desc())
                .fetch()
                .stream()
                .flatMap(msg -> Stream.of(msg.getSenderProfile(), msg.getReceiverProfile()))
                .filter(profile -> !profile.equals(myProfile))
                .distinct()
                .toList();

        return profiles;
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
