package com.capstone.wanf.firebase.service;

import com.capstone.wanf.error.exception.RestApiException;
import com.capstone.wanf.profile.service.ProfileService;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.capstone.wanf.fixture.DomainFixture.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class FCMServiceTest {
    @InjectMocks
    private FCMService fcmService;

    @Mock
    private ProfileService profileService;

    @Mock
    private FirebaseMessaging firebaseMessaging;

    @Test
    void 쪽지_푸시_알람을_보낸다() throws FirebaseMessagingException {
        //given
        given(profileService.findById(anyLong())).willReturn(프로필2);

        given(profileService.findByUser(any())).willReturn(프로필1);
        //when
        fcmService.sendMessageNotification(카프카_쪽지1);
        //then
        verify(firebaseMessaging).sendMulticast(any());
    }

    @Test
    void 댓글_푸시_알람을_보낸다() throws FirebaseMessagingException {
        //given & when
        fcmService.sendCommentNotification(프로필1,프로필2,댓글1,1L);
        //then
        verify(firebaseMessaging).sendMulticast(any());
    }

    @Test
    void 푸시_알람_전송_시에_오류_발생시_커스텀_예외를_발생시킨다() throws FirebaseMessagingException {
        //given
        given(firebaseMessaging.sendMulticast(any())).willThrow(FirebaseMessagingException.class);
        //when & then
        assertThrows(RestApiException.class, () -> fcmService.sendCommentNotification(프로필1,프로필2,댓글1,1L));
    }

}