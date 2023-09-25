package com.capstone.wanf.message.service;

import com.capstone.wanf.error.exception.RestApiException;
import com.capstone.wanf.firebase.service.FCMService;
import com.capstone.wanf.message.converter.MessageConverter;
import com.capstone.wanf.message.domain.entity.KafkaMessage;
import com.capstone.wanf.message.domain.repo.MessageRepository;
import com.capstone.wanf.message.domain.repo.MessageRepositorySupport;
import com.capstone.wanf.message.dto.response.ReceiverMessageResponse;
import com.capstone.wanf.profile.dto.response.ProfileResponse;
import com.capstone.wanf.profile.service.ProfileService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.List;

import static com.capstone.wanf.fixture.DomainFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
public class MessageServiceTest {
    @InjectMocks
    private MessageConsumerService messageConsumerService;

    @InjectMocks
    private MessageProducerService messageProducerService;

    @Mock
    private MessageRepository messageRepository;

    @Mock
    private MessageConverter messageConverter;

    @Mock
    private MessageRepositorySupport messageRepositorySupport;

    @Mock
    private ProfileService profileService;

    @Mock
    private FCMService fcmService;

    @Mock
    private  KafkaTemplate<String, KafkaMessage> kafkaTemplate;

    @Test
    void 자신에게_쪽지를_보낼_수_없다(){
        //given
        given(profileService.findByUser(any())).willReturn(프로필1);
        //when & then
        assertThatThrownBy(() -> messageProducerService.sendMessage(쪽지_요청1, 유저1))
                .isInstanceOf(RestApiException.class);
    }

    @Test
    void 쪽지_요청을_받으면_쪽지를_저장한다(){
        //given
        given(profileService.findByUser(any())).willReturn(프로필2);

        //when
        messageProducerService.sendMessage(쪽지_요청1, 유저2);
        //then
        verify(kafkaTemplate).send(any(), any(), any());
    }

    @Test
    void 쪽지를_조회한다() {
        //given
        given(profileService.findById(anyLong())).willReturn(프로필1);

        given(profileService.findByUser(any())).willReturn(프로필2);

        given(messageRepositorySupport.findMessagesByReceiverAndSender(any(), any())).willReturn(쪽지_목록1);
        //when
        ReceiverMessageResponse 수신자_쪽지_응답_목록 = messageConsumerService.getMessage(유저1, 1L);
        //then
        assertAll(
                () -> assertThat(수신자_쪽지_응답_목록.receiverProfileId()).isEqualTo(프로필2.getId()),
                () -> assertThat(수신자_쪽지_응답_목록.messages()).isEqualTo(쪽지_목록1)
        );
    }

    @Test
    void 쪽지를_주고받은_사람들을_조회한다(){
        //given
        given(profileService.findByUser(any())).willReturn(프로필1);

        given(messageRepositorySupport.findSenderByReceiver(any())).willReturn(List.of(프로필1));
        //when
        List<ProfileResponse> 쪽지를_주고받은_사람들 = messageConsumerService.getSenders(유저1);
        //then
        assertThat(쪽지를_주고받은_사람들.size()).isEqualTo(1);
    }

    @Test
    void 쪽지를_송신하면_푸시_알람을_보내고_쪽지를_저장한다(){
        //given
        given(messageConverter.convertMessage(any())).willReturn(쪽지1);
        //when
        messageConsumerService.receive(카프카_쪽지1);
        //then
        verify(messageRepository).save(any());
    }
}
