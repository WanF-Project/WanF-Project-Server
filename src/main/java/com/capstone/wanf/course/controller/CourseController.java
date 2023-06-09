package com.capstone.wanf.course.controller;

import com.capstone.wanf.course.domain.entity.Course;
import com.capstone.wanf.course.dto.request.CourseRequest;
import com.capstone.wanf.course.service.CourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
            summary = "모든 강의 조회",
            description = "모든 강의를 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "요청 성공")
            }
    )
    public ResponseEntity<List<Course>> findAll() {
        List<Course> courses = courseService.findAll();

        return ResponseEntity.ok(courses);
    }

    @PostMapping("/courses")
    @PreAuthorize("isAuthenticated() and hasRole('ROLE_ADMIN')")
    @Operation(
            summary = "강의 생성",
            description = "강의을 생성합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "요청 성공")
            }
    )
    public ResponseEntity<Course> createCourse(@Valid @RequestBody CourseRequest courseRequest) {
        Course createdCourse = courseService.save(courseRequest);

        return ResponseEntity.ok(createdCourse);
    }

    @PreAuthorize("isAuthenticated() and hasRole('ROLE_ADMIN')")
    @DeleteMapping("/courses/{id}")
    @Operation(
            summary = "강의 삭제",
            description = "강의을 삭제합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "요청 성공")
            }
    )
    public ResponseEntity<Void> deleteCourse(@PathVariable(name = "id") Long id) {
        courseService.deleteCourse(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/courses/search")
    @Operation(
            summary = "강의 검색",
            description = "강의를 검색합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "요청 성공")
            }
    )
    public ResponseEntity<List<Course>> searchCourse(@RequestParam(name = "query") String query) {
        List<Course> courses = courseService.searchByQuery(query);

        return ResponseEntity.ok(courses);
    }
}
