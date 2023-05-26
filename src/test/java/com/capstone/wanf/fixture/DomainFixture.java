package com.capstone.wanf.fixture;

import com.capstone.wanf.course.domain.entity.Course;
import com.capstone.wanf.course.domain.entity.Major;
import com.capstone.wanf.course.dto.request.RequestCourse;

public class DomainFixture {
    public static final Major 전공1 = Major.builder()
            .name("전공1")
            .build();

    public static final Course 수업1 = Course.builder()
            .name("수업1")
            .category("카테고리1")
            .courseTime("수업시간1")
            .courseId("과목코드1")
            .professor("교수1")
            .build();

    public static final RequestCourse 수업_요청1 = new RequestCourse("수업명", "카테고리", "수업시간", "과목코드", "교수");


}
