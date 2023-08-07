package com.capstone.wanf.message.domain.repo;

import com.capstone.wanf.message.domain.entity.QMessage;
import com.capstone.wanf.user.domain.entity.User;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MessageRepositorySupport {
    private final JPAQueryFactory jpaQueryFactory;

    private final QMessage message = QMessage.message;

    public List<User> findSenderByReceiver(User user) {
        List<User> senders = jpaQueryFactory.select(message.sender)
                .from(message)
                .where(message.receiver.eq(user))
                .orderBy(message.modifiedDate.desc())
                .distinct()
                .fetch();

        return senders;
    }
}
