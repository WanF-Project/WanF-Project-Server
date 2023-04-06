package com.capstone.wanf.course.controller;

import com.capstone.wanf.course.domain.entity.Course;
import com.capstone.wanf.course.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class CourseController {
    private final CourseService courseService;

    @GetMapping("/courses/{id}")
    public ResponseEntity<Course> findById(@PathVariable(name = "id") Long id) {
        Course course = courseService.findById(id);

        return ResponseEntity.ok(course);
    }

    @GetMapping("/courses")
    public ResponseEntity<List<Course>> findAll() {
        List<Course> courses = courseService.findAll();

        return ResponseEntity.ok(courses);
    }
}
