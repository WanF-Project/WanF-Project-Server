package com.capstone.wanf.storage.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.capstone.wanf.error.exception.RestApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;

import static com.capstone.wanf.error.errorcode.CommonErrorCode.IO_EXCEPTION;
import static com.capstone.wanf.error.errorcode.CustomErrorCode.FILE_DELETE_FAILED;
import static com.capstone.wanf.error.errorcode.CustomErrorCode.INVALID_FILE_TYPE;

@RequiredArgsConstructor
@Service
public class S3Service {
    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String upload(MultipartFile multipartFile, String dirName) {
        try {
            validateImage(multipartFile.getContentType());

            File uploadFile = convert(multipartFile)
                    .orElseThrow(() -> new RestApiException(INVALID_FILE_TYPE));

            return upload(uploadFile, dirName);
        } catch (IOException e) {
            throw new RestApiException(IO_EXCEPTION);
        }
    }

    private String upload(File uploadFile, String dirName) {
        String fileName = dirName + "/" + uploadFile.getName();     // S3에 저장될 파일 이름 (dirName/파일명.확장자)

        String uploadImageUrl = putS3(uploadFile, fileName);        // S3에 파일 업로드

        removeNewFile(uploadFile);

        return uploadImageUrl;                            // 업로드된 파일의 S3 URL 주소 반환
    }

    private String putS3(File uploadFile, String fileName) {
        amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, uploadFile)
                .withCannedAcl(CannedAccessControlList.PublicRead));    // PublicRead 권한으로 업로드

        String path = amazonS3Client.getUrl(bucket, fileName).getPath();    // 업로드된 파일의 S3 URL 주소 가져오기

        return "https://d1csu9i9ktup9e.cloudfront.net" + path;    // CDN 주소로 변환
    }

    // 변환 과정에서 로컬에 생성된 File 삭제
    private void removeNewFile(File targetFile) {
        if (!targetFile.delete()) {
            throw new RestApiException(FILE_DELETE_FAILED);
        }
    }

    // S3는 MultipartFile을 지원하지 않으므로 File로 변환
    private Optional<File> convert(MultipartFile file) throws IOException {
        // 파일명 중복을 피하기 위해 파일명 앞에 현재시간을 붙임
        File convertFile = new File(System.currentTimeMillis() + "_" + file.getOriginalFilename());

        if (convertFile.createNewFile()) {
            try (FileOutputStream fos = new FileOutputStream(convertFile)) {
                fos.write(file.getBytes());
            }

            return Optional.of(convertFile);
        }
        return Optional.empty();
    }

    // 이미지 파일인지 검증
    private void validateImage(String contentType) {
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new RestApiException(INVALID_FILE_TYPE);
        }
    }

    // S3에 저장된 파일 삭제
    public void delete(String fileName) {
        amazonS3Client.deleteObject(bucket, fileName);
    }
}
