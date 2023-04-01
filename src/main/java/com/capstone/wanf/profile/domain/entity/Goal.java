package com.capstone.wanf.profile.domain.entity;

import lombok.Getter;

@Getter
public enum Goal {
    TOP_OF_THE_MAJOR("과탑"),
    MAKING_FRIENDS("친목"),
    AREA_OF_INTEREST("관심분야"),
    JUST_TAKING_CLASS("그냥"),
    GRADUATE("졸업"),
    CAREER_DISCOVERY("진로 탐색");

    private final String detail;

    Goal(String detail) {
        this.detail = detail;
    }
}
