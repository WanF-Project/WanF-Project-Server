package com.capstone.wanf.storage.controller;

import com.capstone.wanf.storage.domain.Directory;
import com.capstone.wanf.storage.dto.response.ImageResponse;
import com.capstone.wanf.storage.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class ImageController {
    private final S3Service s3Service;

    @PostMapping(value = "/images", consumes = {"multipart/form-data"})
    public ResponseEntity<ImageResponse> imageUpload(@RequestParam("directory") Directory directory,
                                                     @RequestPart("uploadImage") MultipartFile uploadImage) {   // RequestPart: MultipartFile이 포함되는 경우
        String imageUrl = s3Service.upload(uploadImage, directory);
        ImageResponse imageResponse = ImageResponse.builder()
                .imageUrl(imageUrl)
                .build();

        return ResponseEntity.ok(imageResponse);
    }
}
