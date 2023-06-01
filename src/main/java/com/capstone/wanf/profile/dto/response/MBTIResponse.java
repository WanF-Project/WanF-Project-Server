package com.capstone.wanf.profile.dto.response;

import lombok.Builder;

@Builder
public record MBTIResponse(
        Long id,
        String name
) {
}
