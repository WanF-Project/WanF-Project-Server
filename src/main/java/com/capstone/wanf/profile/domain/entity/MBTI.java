package com.capstone.wanf.profile.domain.entity;

import lombok.Getter;

@Getter
public enum MBTI {
    ISTJ("현실주의자"),
    ISTP("장인"),
    INFJ("옹호자"),
    INTJ("전략가"),
    ISFJ("수호자"),
    ISFP("모험가"),
    INFP("중재자"),
    INTP("논리술사"),
    ESTJ("경영가"),
    ESFP("연예인"),
    ENFP("활동가"),
    ENTP("변론가"),
    ESFJ("집정관"),
    ESTP("사업가"),
    ENFJ("선도자"),
    ENTJ("통솔자");

    private final String detail;

    MBTI(String detail) {
        this.detail = detail;
    }
}
