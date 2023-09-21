package com.capstone.wanf.course.controller;

import com.capstone.wanf.course.domain.entity.Course;
import com.capstone.wanf.course.dto.request.CourseRequest;
import com.capstone.wanf.course.service.CourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "강의", description = "강의 API")
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class CourseController {
    private final CourseService courseService;

    @GetMapping("/courses/{id}")
    @Operation(summary = "특정 강의 조회")
    public ResponseEntity<Course> findById(@PathVariable(name = "id") Long id) {
        Course course = courseService.findById(id);

        return ResponseEntity.ok(course);
    }

    @GetMapping("/courses")
    @Operation(
            summary = "모든 강의 조회",
            description = "모든 강의를 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "모든 강의 조회 성공 만약 강의가 존재하지 않는다면 빈 리스트를 반환합니다.")
            }
    )
    public ResponseEntity<List<Course>> findAll() {
        List<Course> courses = courseService.findAll();

        return ResponseEntity.ok(courses);
    }

    @PostMapping("/courses")
    @PreAuthorize("isAuthenticated() and hasRole('ROLE_ADMIN')")
    @Operation(summary = "강의 생성(관리자만 접근 가능)")
    public ResponseEntity<Course> createCourse(@Valid @RequestBody CourseRequest courseRequest) {
        Course createdCourse = courseService.save(courseRequest);

        return ResponseEntity.ok(createdCourse);
    }

    @PreAuthorize("isAuthenticated() and hasRole('ROLE_ADMIN')")
    @DeleteMapping("/courses/{id}")
    @Operation(summary = "강의 삭제")
    public ResponseEntity<Void> deleteCourse(@PathVariable(name = "id") Long id) {
        courseService.deleteCourse(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/courses/search")
    @Operation(summary = "강의 검색")
    public ResponseEntity<List<Course>> searchCourse(@RequestParam(name = "query") String query) {
        List<Course> courses = courseService.searchByQuery(query);

        return ResponseEntity.ok(courses);
    }
}
