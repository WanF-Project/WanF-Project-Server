package com.capstone.wanf.fixture;

import com.capstone.wanf.course.domain.entity.Course;
import com.capstone.wanf.major.domain.entity.Major;

public class DomainFixture {
    public static final Major 전공1 = Major.builder()
            .name("전공1")
            .build();

    public static final Course 수업1 = Course.builder()
            .major(전공1)
            .name("수업1")
            .category("카테고리1")
            .courseTime("수업시간1")
            .courseId("과목코드1")
            .professor("교수1")
            .build();
}
