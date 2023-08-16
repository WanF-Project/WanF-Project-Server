package com.capstone.wanf.storage.controller;

import com.capstone.wanf.storage.domain.entity.Directory;
import com.capstone.wanf.storage.domain.entity.Image;
import com.capstone.wanf.storage.dto.response.ImageResponse;
import com.capstone.wanf.storage.service.S3Service;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
    @Operation(
            summary = "이미지 업로드",
            description = "이미지를 업로드합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "요청 성공"),
                    @ApiResponse(responseCode = "400", ref = "400"),
                    @ApiResponse(responseCode = "403", ref = "403")
            }
    )
    public ResponseEntity<ImageResponse> imageUpload(@RequestParam("directory") Directory directory,
                                                     @RequestPart("uploadImage") MultipartFile uploadImage) {   // RequestPart: MultipartFile이 포함되는 경우
        Image image = s3Service.upload(uploadImage, directory);
        
        ImageResponse imageResponse = ImageResponse.builder()
                .imageId(image.getId())
                .imageUrl(image.getImageUrl())
                .build();

        return ResponseEntity.ok(imageResponse);
    }
}
