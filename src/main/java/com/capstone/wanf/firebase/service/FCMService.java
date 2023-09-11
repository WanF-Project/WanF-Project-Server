package com.capstone.wanf.firebase.service;

import com.capstone.wanf.comment.domain.entity.Comment;
import com.capstone.wanf.error.exception.RestApiException;
import com.capstone.wanf.message.domain.entity.KafkaMessage;
import com.capstone.wanf.profile.domain.entity.Profile;
import com.capstone.wanf.profile.service.ProfileService;
import com.google.firebase.messaging.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

import static com.capstone.wanf.error.errorcode.CustomErrorCode.*;

@Service
@RequiredArgsConstructor
public class FCMService {
    private final FirebaseMessaging firebaseMessaging;

    private final ProfileService profileService;

    public void sendMessageNotification(KafkaMessage kafkaMessage) {
        Profile receiverProfile = profileService.findById(kafkaMessage.receiverProfileId());

        Profile senderProfile = profileService.findByUser(kafkaMessage.sender());

        List<String> fcmTokens = receiverProfile.getUser().getFcmTokens();

        MulticastMessage message = MulticastMessage.builder()
                .putAllData(new HashMap<>() {{
                    put("time", LocalDateTime.now().toString());
                    put("senderProfileId", senderProfile.getId().toString());
                }})
                .setNotification(Notification.builder()
                        .setTitle(senderProfile.getNickname() + "님이 쪽지를 보내셨습니다.")
                        .setBody(kafkaMessage.content())
                        .build())
                .addAllTokens(fcmTokens)
                .build();

        sendMessage(message);
    }

    public void sendCommentNotification(Profile writerProfile, Profile commenterProfile, Comment comment, Long postId) {
        List<String> fcmTokens = writerProfile.getUser().getFcmTokens();

        MulticastMessage message = MulticastMessage.builder()
                .putAllData(new HashMap<>() {{
                    put("time", LocalDateTime.now().toString());
                    put("postId", postId.toString());
                }})
                .setNotification(Notification.builder()
                        .setTitle(commenterProfile.getNickname() + "님이 댓글을 달았습니다.")
                        .setBody(comment.getContent())
                        .build())
                .addAllTokens(fcmTokens)
                .build();


        sendMessage(message);
    }

    private void sendMessage(MulticastMessage message) {
        try {
            firebaseMessaging.sendMulticast(message);
        } catch (FirebaseMessagingException e) {
            throw new RestApiException(FIREBASE_MESSAGE_SEND_FAILED);
        }
    }
}
