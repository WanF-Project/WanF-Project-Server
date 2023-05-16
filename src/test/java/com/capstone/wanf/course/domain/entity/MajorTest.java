package com.capstone.wanf.course.domain.entity;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class MajorTest {
    public static final String MAJOR_NAME = "전공명";

    @Test
    void 전공을_생성할_수_있다(){
        //when & then
        assertThatCode(() -> Major.builder()
                .name(MAJOR_NAME)
                .build())
                .doesNotThrowAnyException();
    }

    @Test
    void 전공에_저장된_정보를_조회할_수_있다() {
        //given
        Major major = Major.builder()
                .name(MAJOR_NAME)
                .build();
        //when & then
        assertThat(major.getName()).isEqualTo(MAJOR_NAME);
    }

}