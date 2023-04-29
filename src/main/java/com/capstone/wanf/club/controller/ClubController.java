package com.capstone.wanf.club.controller;

import com.capstone.wanf.club.domain.entity.Club;
import com.capstone.wanf.club.service.ClubService;
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
    public ResponseEntity<List<Club>> findAll() {
        List<Club> clubs = clubService.findAll();

        return ResponseEntity.ok(clubs);
    }
}
