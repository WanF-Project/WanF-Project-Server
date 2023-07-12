package com.capstone.wanf.course.service;

import com.capstone.wanf.course.domain.entity.Course;
import com.capstone.wanf.course.domain.entity.Major;
import com.capstone.wanf.course.domain.repo.CourseRepository;
import com.capstone.wanf.course.domain.repo.CourseRepositorySupport;
import com.capstone.wanf.course.dto.request.CourseRequest;
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

    private final CourseRepositorySupport courseRepositorySupport;

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
    public Course save(CourseRequest courseRequest) {
        Course course;

        if (courseRequest.getMajorId() != null) {
            Major major = majorService.findById(courseRequest.getMajorId());

            course = Course.builder()
                    .courseId(courseRequest.getCourseId())
                    .name(courseRequest.getName())
                    .category(courseRequest.getCategory())
                    .courseTime(courseRequest.getCourseTime())
                    .professor(courseRequest.getProfessor())
                    .major(major)
                    .build();
        } else {
            course = Course.builder()
                    .courseId(courseRequest.getCourseId())
                    .name(courseRequest.getName())
                    .category(courseRequest.getCategory())
                    .courseTime(courseRequest.getCourseTime())
                    .professor(courseRequest.getProfessor())
                    .build();
        }

        return courseRepository.save(course);
    }

    @Transactional
    public void deleteCourse(Long id) {
        courseRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Course> searchByQuery(String query) {
       return courseRepositorySupport.searchByQuery(query);
    }
}
