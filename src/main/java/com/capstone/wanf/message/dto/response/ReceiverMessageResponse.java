package com.capstone.wanf.message.dto.response;

import com.capstone.wanf.profile.dto.response.ProfileResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.List;

@Builder
@Schema(description = "송신자와 수신자 간의 쪽지 응답 데이터")
public record ReceiverMessageResponse(
        @Schema(description = "송신자 프로필")
        ProfileResponse senderProfile,
        @Schema(description = "송신자가 보낸 쪽지들")
        List<MessageResponse> messages
) {
}
