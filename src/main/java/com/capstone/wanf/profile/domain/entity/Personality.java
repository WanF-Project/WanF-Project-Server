package com.capstone.wanf.profile.domain.entity;

import lombok.Getter;

@Getter
public enum Personality {
    SILENT("조용한"),
    BRIGHT("밝은"),
    RELAXED("느긋한"),
    EFFICIENCY("효율중시"),
    LEADERSHIP("리더십 있는"),
    METICULOUSNESS("꼼꼼한"),
    BRAVERY("명랑한"),
    PERFECTIONISM("완벽주의"),
    ACTIVE("적극적인"),
    IMPULSIVE("충동적인"),
    PASSIONATE("열정적인"),
    HONEST("솔직한"),
    CREATIVE("창의적인"),
    OPTIMISTIC("낙천적인"),
    SENTIMENTAL("감성적인"),
    THOUGHTFUL("사려깊은"),
    FRIENDLY("친근한"),
    FREE("자유로운"),
    DREAMER("몽상가"),
    PLANNED("계획적인"),
    NARCISSISM("자기애가 강한"),
    CHALLENGE("도전적인"),
    LOGICAL("논리적인"),
    EFFORT("노력형"),
    EFFICIENT("효율적인"),
    RESPONSIBLE("책임감 있는"),
    CONSIDERATE("배려심 깊은");


    private final String detail;

    Personality(String detail) {
        this.detail = detail;
    }
}
