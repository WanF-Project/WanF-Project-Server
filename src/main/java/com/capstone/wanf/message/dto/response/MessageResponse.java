package com.capstone.wanf.message.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDateTime;
@Builder
@Schema(description = "쪽지 응답 데이터")
public record MessageResponse(
        @Schema(description = "쪽지 내용")
        String content,
        @Schema(description = "생성 날짜")
        LocalDateTime createDate,
        @Schema(description = "수정 날짜")
        LocalDateTime modifiedDate
) {
}
