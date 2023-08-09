package com.capstone.wanf.storage.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.capstone.wanf.error.exception.RestApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

import static com.capstone.wanf.error.errorcode.CommonErrorCode.IO_EXCEPTION;
import static com.capstone.wanf.error.errorcode.CustomErrorCode.INVALID_FILE_TYPE;

@RequiredArgsConstructor
@Service
public class S3Service {
    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String upload(MultipartFile multipartFile, String dirName) {
        validateImage(multipartFile.getContentType());

        String fileName = createFileName(multipartFile.getOriginalFilename(), dirName);

        ObjectMetadata objectMetadata = new ObjectMetadata();

        objectMetadata.setContentLength(multipartFile.getSize());

        objectMetadata.setContentType(multipartFile.getContentType());

        // s3에 이미지 저장
        try (InputStream inputStream = multipartFile.getInputStream()) {
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, fileName, inputStream, objectMetadata);

            putObjectRequest.setCannedAcl(CannedAccessControlList.PublicRead); // 퍼블릭 읽기 권한 설정

            amazonS3Client.putObject(putObjectRequest);

            String path = amazonS3Client.getUrl(bucket, fileName).getPath();

            return "https://d1csu9i9ktup9e.cloudfront.net" + path;
        } catch (IOException e) {
            throw new RestApiException(IO_EXCEPTION);
        }
    }

    // 이미지 파일인지 검증
    private void validateImage(String contentType) {
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new RestApiException(INVALID_FILE_TYPE);
        }
    }

    // S3 bucket에 저장된 파일 삭제
    public void delete(String dirName, String fileName) {
        amazonS3Client.deleteObject(bucket, dirName + "/" + fileName);
    }

    // S3 bucket에 저장될 파일 이름 생성 (파일 이름 중복 방지)
    private String createFileName(String fileName, String dirName) {
        return dirName + "/" + System.currentTimeMillis() + "_" + fileName;
    }
}
