package com.capstone.wanf.course.controller;

import com.capstone.wanf.course.domain.entity.Major;
import com.capstone.wanf.course.dto.request.MajorRequest;
import com.capstone.wanf.course.service.MajorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "전공", description = "전공 API")
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class MajorController {
    private final MajorService majorService;

    @GetMapping("/majors")
    @Operation(summary = "모든 전공 조회")
    public ResponseEntity<List<Major>> findAll() {
        List<Major> majors = majorService.findAll();

        return ResponseEntity.ok(majors);
    }

    @PostMapping("/majors")
    @PreAuthorize("isAuthenticated() and hasRole('ROLE_ADMIN')")
    @Operation(summary = "전공 생성")
    public ResponseEntity<Major> save(@Valid @RequestBody MajorRequest majorRequest) {
        Major createdMajor = majorService.save(majorRequest.toEntity());

        return ResponseEntity.ok(createdMajor);
    }

    @DeleteMapping("/majors/{id}")
    @PreAuthorize("isAuthenticated() and hasRole('ROLE_ADMIN')")
    @Operation(summary = "전공 삭제")
    public ResponseEntity<Void> deleteById(@PathVariable(name = "id") Long id) {
        majorService.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
