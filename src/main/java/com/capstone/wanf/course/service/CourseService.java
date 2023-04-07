package com.capstone.wanf.course.service;

import com.capstone.wanf.course.domain.entity.Course;
import com.capstone.wanf.course.domain.repo.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class CourseService {
    private final CourseRepository courseRepository;

    @Transactional(readOnly = true)
    public Course findById(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("해당 수업이 존재하지 않습니다."));
    }

    public List<Course> findAll() {
        return courseRepository.findAll();
    }
}
