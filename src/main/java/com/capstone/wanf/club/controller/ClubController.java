package com.capstone.wanf.club.controller;

import com.capstone.wanf.club.domain.entity.Club;
import com.capstone.wanf.club.service.ClubService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/api/v1")
@RequiredArgsConstructor
@RestController
public class ClubController {
    private final ClubService clubService;

    @GetMapping("/groups")
    @Operation(
            summary = "모든 모임 조회",
            description = "모든 모임을 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "요청 성공"),
            }
    )
    public ResponseEntity<List<Club>> findAll() {
        List<Club> clubs = clubService.findAll();

        return ResponseEntity.ok(clubs);
    }
}
