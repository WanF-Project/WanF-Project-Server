package com.capstone.wanf.message.converter;

import com.capstone.wanf.message.domain.entity.Message;
import com.capstone.wanf.message.dto.response.MessageResponse;
import com.capstone.wanf.profile.service.ProfileService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.capstone.wanf.fixture.DomainFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class MessageConverterTest {
    @InjectMocks
    private MessageConverter messageConverter;

    @Mock
    private ProfileService profileService;

    @Test
    void 카프카_쪽지를_쪽지_엔티티로_변환한다(){
        //given
        given(profileService.findById(anyLong())).willReturn(프로필1);

        given(profileService.findByUser(any())).willReturn(프로필2);
        //when
        Message message = messageConverter.convertMessage(카프카_쪽지1);
        //then
        assertAll(
                () -> assertThat(message.getReceiverProfile()).isEqualTo(프로필1),
                () -> assertThat(message.getSenderProfile()).isEqualTo(프로필2),
                () -> assertThat(message.getContent()).isEqualTo(카프카_쪽지1.content())
        );
    }

    @Test
    void 쪽지_엔티티를_쪽지_응답_데이터로_변환한다(){
        //given & when
        MessageResponse messageResponse = messageConverter.convertMessageResponse(쪽지1);
        //then
        assertAll(
                () -> assertThat(messageResponse.senderProfileId()).isEqualTo(프로필2.getId()),
                () -> assertThat(messageResponse.content()).isEqualTo(쪽지1.getContent())
        );
    }
}
