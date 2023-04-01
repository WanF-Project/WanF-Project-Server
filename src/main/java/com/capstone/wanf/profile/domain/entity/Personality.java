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
    BRAVERY("명량한");


    private final String detail;

    Personality(String detail) {
        this.detail = detail;
    }
}
