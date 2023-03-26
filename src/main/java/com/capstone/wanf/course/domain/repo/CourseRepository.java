package com.capstone.wanf.course.domain.repo;

import com.capstone.wanf.course.domain.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {
}
