package com.capstone.wanf.club.domain.entity;

import lombok.Getter;

@Getter
public enum Authority {
    CLUB_LEADER("모임장"),
    CLUB_MEMBER("모임원"),
    NONE("권한 없음");

    private final String detail;

    Authority(String detail) {
        this.detail = detail;
    }
}
