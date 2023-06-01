package com.capstone.wanf.profile.domain.entity;

import lombok.Getter;

@Getter
public enum MBTI {
    ISTJ("현실주의자", 1L),
    ISTP("장인", 2L),
    INFJ("옹호자", 3L),
    INTJ("전략가", 4L),
    ISFJ("수호자", 5L),
    ISFP("모험가", 6L),
    INFP("중재자", 7L),
    INTP("논리술사", 8L),
    ESTJ("경영가", 9L),
    ESFP("연예인", 10L),
    ENFP("활동가", 11L),
    ENTP("변론가", 12L),
    ESFJ("집정관", 13L),
    ESTP("사업가", 14L),
    ENFJ("선도자", 15L),
    ENTJ("통솔자", 16L),
    ;

    private final String detail;

    private final Long id;

    MBTI(String detail, Long id) {
        this.detail = detail;
        this.id = id;
    }
}
