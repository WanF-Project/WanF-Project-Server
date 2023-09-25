package com.capstone.wanf.message.dto.response;

import com.capstone.wanf.message.domain.entity.Message;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDateTime;
@Builder
@Schema(description = "쪽지 응답 데이터")
public record MessageResponse(
        @Schema(description = "쪽지를 보낸 사람의 프로필 id")
        Long senderProfileId,
        @Schema(description = "쪽지 내용")
        String content,
        @Schema(description = "생성 날짜")
        LocalDateTime createDate,
        @Schema(description = "수정 날짜")
        LocalDateTime modifiedDate
) {
        public static MessageResponse of(Message message) {
                return MessageResponse.builder()
                        .senderProfileId(message.getSenderProfile().getId())
                        .content(message.getContent())
                        .createDate(message.getCreatedDate())
                        .modifiedDate(message.getModifiedDate())
                        .build();
        }
}
