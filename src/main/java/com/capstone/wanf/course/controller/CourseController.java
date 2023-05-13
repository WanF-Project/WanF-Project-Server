package com.capstone.wanf.course.controller;

import com.capstone.wanf.course.domain.entity.Course;
import com.capstone.wanf.course.service.CourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class CourseController {
    private final CourseService courseService;

    @GetMapping("/courses/{id}")
    @Operation(
            summary = "특정 강의 조회",
            description = "해당 ID의 강의를 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "요청 성공"),
                    @ApiResponse(responseCode = "404", ref = "404")
            }
    )
    public ResponseEntity<Course> findById(@PathVariable(name = "id") Long id) {
        Course course = courseService.findById(id);

        return ResponseEntity.ok(course);
    }

    @GetMapping("/courses")
    @Operation(
            summary = "모든 수업 조회",
            description = "모든 수업을 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "요청 성공")
            }
    )
    public ResponseEntity<List<Course>> findAll() {
        List<Course> courses = courseService.findAll();

        return ResponseEntity.ok(courses);
    }

    @PostMapping("/courses")
    @Operation(
            summary = "수업 생성",
            description = "수업을 생성합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "요청 성공")
            }
    )
    public ResponseEntity<Course> createCourse(@RequestBody Course course) {
        Course createdCourse = courseService.createCourse(course);

        return ResponseEntity.ok(createdCourse);
    }

    @DeleteMapping("/courses/{id}")
    @Operation(
            summary = "수업 삭제",
            description = "수업을 삭제합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "요청 성공")
            }
    )
    public ResponseEntity<Void> deleteCourse(@PathVariable(name = "id") Long id) {
        courseService.deleteCourse(id);

        return ResponseEntity.noContent().build();
    }
}
