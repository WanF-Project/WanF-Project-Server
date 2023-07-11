package com.capstone.wanf.course.domain.repo;

import com.capstone.wanf.course.domain.entity.Course;
import com.capstone.wanf.course.domain.entity.QCourse;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CourseRepositorySupport {
    private final JPAQueryFactory jpaQueryFactory;

    private final QCourse course = QCourse.course;

    public List<Course> searchByQuery(String query) {
        List<Course> searchedCourses = jpaQueryFactory.selectFrom(course)
                .where(course.name.contains(query))
                .fetch();

        return searchedCourses;
    }
}
