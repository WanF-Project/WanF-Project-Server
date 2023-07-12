package com.capstone.wanf.course.domain.entity;

import org.junit.jupiter.api.Test;

import static com.capstone.wanf.fixture.DomainFixture.전공1;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class CourseTest {
    public static final String COURSE_ID = "강의코드";

    public static final String COURSE_NAME = "강의명";

    public static final String PROFESSOR = "교수";

    public static final String COURSE_TIME = "강의시간";

    public static final String CATEGORY = "카테고리";

    @Test
    void 강의를_생성할_수_있다(){
        //given
        Major 전공1 = Major.builder()
                .name("전공1")
                .build();
        //when & then
        assertThatCode(() -> Course.builder()
                .courseId(COURSE_ID)
                .name(COURSE_NAME)
                .professor(PROFESSOR)
                .courseTime(COURSE_TIME)
                .category(CATEGORY)
                .major(전공1)
                .build())
                .doesNotThrowAnyException();
    }

    @Test
    void 강의에_저장된_정보를_조회할_수_있다(){
        //given
        Course course = Course.builder()
                .courseId(COURSE_ID)
                .name(COURSE_NAME)
                .professor(PROFESSOR)
                .courseTime(COURSE_TIME)
                .category(CATEGORY)
                .major(전공1)
                .build();
       //when & then
        assertAll(
                () -> assertThat(course.getCourseId()).isEqualTo(COURSE_ID),
                () -> assertThat(course.getName()).isEqualTo(COURSE_NAME),
                () -> assertThat(course.getProfessor()).isEqualTo(PROFESSOR),
                () -> assertThat(course.getCourseTime()).isEqualTo(COURSE_TIME),
                () -> assertThat(course.getCategory()).isEqualTo(CATEGORY),
                () -> assertThat(course.getMajor()).isEqualTo(전공1)
        );
    }

}