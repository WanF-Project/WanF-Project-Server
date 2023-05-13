package com.capstone.wanf.course.controller;

import com.capstone.wanf.course.domain.entity.Major;
import com.capstone.wanf.course.service.MajorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
                    @ApiResponse(responseCode = "404", ref = "404")
            }
    )
    public ResponseEntity<List<Major>> findAll() {
        List<Major> majors = majorService.findAll();

        return ResponseEntity.ok(majors);
    }
}
