package com.capstone.wanf.banner.controller;

import com.capstone.wanf.banner.dto.response.BannerResponse;
import com.capstone.wanf.banner.service.BannerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "배너", description = "배너 API")
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class BannerController {
    private final BannerService bannerService;

    @GetMapping("/banners")
    @Operation(summary = "배너 조회")
    public ResponseEntity<List<BannerResponse>> findAll() {
        List<BannerResponse> banners = bannerService.findAll().stream()
                .map(BannerResponse::of)
                .collect(Collectors.toList());

        return ResponseEntity.ok(banners);
    }
}
