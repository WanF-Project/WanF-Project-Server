package com.capstone.wanf.course.controller;

import com.capstone.wanf.course.domain.entity.Major;
import com.capstone.wanf.course.dto.request.MajorRequest;
import com.capstone.wanf.course.service.MajorService;
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
public class MajorController {
    private final MajorService majorService;

    @GetMapping("/majors")
    @Operation(
            summary = "모든 전공 조회",
            description = "모든 전공을 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "요청 성공"),
            }
    )
    public ResponseEntity<List<Major>> findAll() {
        List<Major> majors = majorService.findAll();

        return ResponseEntity.ok(majors);
    }

    @PostMapping("/majors")
    @PreAuthorize("isAuthenticated() and hasRole('ROLE_ADMIN')")
    @Operation(
            summary = "전공 생성",
            description = "전공을 생성합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "요청 성공")
            }
    )
    public ResponseEntity<Major> save(@Valid @RequestBody MajorRequest majorRequest) {
        Major createdMajor = majorService.save(majorRequest.toEntity());

        return ResponseEntity.ok(createdMajor);
    }

    @DeleteMapping("/majors/{id}")
    @PreAuthorize("isAuthenticated() and hasRole('ROLE_ADMIN')")
    @Operation(
            summary = "전공 삭제",
            description = "전공을 삭제합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "요청 성공")
            }
    )
    public ResponseEntity<Void> deleteById(@PathVariable(name = "id") Long id) {
        majorService.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
