package com.capstone.wanf.message.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "쪽지 보내기 요청")
public record MessageRequest(
        @Schema(description = "수신자 ID")
        Long receiverProfileId,
        @Schema(description = "쪽지 내용")
        String content
) {
}
