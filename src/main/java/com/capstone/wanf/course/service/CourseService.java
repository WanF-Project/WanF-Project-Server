package com.capstone.wanf.course.service;

import com.capstone.wanf.course.domain.entity.Course;
import com.capstone.wanf.course.domain.entity.Major;
import com.capstone.wanf.course.domain.repo.CourseRepository;
import com.capstone.wanf.course.dto.request.RequestCourse;
import com.capstone.wanf.error.exception.RestApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.capstone.wanf.error.errorcode.CustomErrorCode.COURSE_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class CourseService {
    private final CourseRepository courseRepository;

    private final MajorService majorService;

    @Transactional(readOnly = true)
    public Course findById(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new RestApiException(COURSE_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public List<Course> findAll() {
        return courseRepository.findAll();
    }

    @Transactional
    public Course save(RequestCourse requestCourse) {
        Course course;

        if (requestCourse.getMajorId() != null) {
            Major major = majorService.findById(requestCourse.getMajorId());

            course = Course.builder()
                    .courseId(requestCourse.getCourseId())
                    .name(requestCourse.getName())
                    .category(requestCourse.getCategory())
                    .courseTime(requestCourse.getCourseTime())
                    .professor(requestCourse.getProfessor())
                    .major(major)
                    .build();
        } else {
            course = Course.builder()
                    .courseId(requestCourse.getCourseId())
                    .name(requestCourse.getName())
                    .category(requestCourse.getCategory())
                    .courseTime(requestCourse.getCourseTime())
                    .professor(requestCourse.getProfessor())
                    .build();
        }

        return courseRepository.save(course);
    }

    @Transactional
    public void deleteCourse(Long id) {
        courseRepository.deleteById(id);
    }
}
