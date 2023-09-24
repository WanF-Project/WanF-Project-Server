package com.capstone.wanf.profile.dto.response;

import com.capstone.wanf.profile.domain.entity.MBTI;
import lombok.Builder;

@Builder
public record MBTIResponse(
        Long id,
        String name
) {
    public static MBTIResponse of(MBTI mbti) {
        return MBTIResponse.builder()
                .id(mbti.getId())
                .name(mbti.name())
                .build();
    }
}
