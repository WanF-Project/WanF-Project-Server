package com.capstone.wanf.storage.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.capstone.wanf.error.exception.RestApiException;
import com.capstone.wanf.storage.domain.entity.Directory;
import com.capstone.wanf.storage.domain.entity.Image;
import com.capstone.wanf.storage.domain.repo.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import static com.capstone.wanf.error.errorcode.CommonErrorCode.IO_EXCEPTION;
import static com.capstone.wanf.error.errorcode.CustomErrorCode.IMAGE_NOT_FOUND;
import static com.capstone.wanf.error.errorcode.CustomErrorCode.INVALID_FILE_TYPE;

@RequiredArgsConstructor
@Service
public class S3Service {
    private final AmazonS3Client amazonS3Client;

    private final ImageRepository imageRepository;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Transactional
    public Image upload(MultipartFile multipartFile, Directory directory) {
        validateImage(multipartFile.getContentType());

        String fileName = createFileName(multipartFile.getOriginalFilename(), directory.getName());

        // 메타 데이터 설정
        ObjectMetadata objectMetadata = new ObjectMetadata();

        objectMetadata.setContentLength(multipartFile.getSize());

        objectMetadata.setContentType(multipartFile.getContentType());

        // s3에 이미지 저장
        try (InputStream inputStream = multipartFile.getInputStream()) {
            amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, inputStream, objectMetadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));    // s3에 저장된 파일을 public으로 접근 가능하도록 설정

            String path = amazonS3Client.getUrl(bucket, fileName).getPath();

            Image image = Image.builder()
                    .imageUrl("https://d1csu9i9ktup9e.cloudfront.net" + path)
                    .directory(directory)
                    .convertImageName(fileName.substring(fileName.lastIndexOf("/") + 1))
                    .build();

            imageRepository.save(image);

            return image;
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
    @Transactional
    public void delete(Image image) {
        amazonS3Client.deleteObject(bucket, image.getDirectory().getName() + "/" + image.getConvertImageName());

        imageRepository.deleteById(image.getId());
    }

    // S3 bucket에 저장될 파일 이름 생성 (파일 이름 중복 방지)
    private String createFileName(String fileName, String dirName) {
        return dirName + "/" + UUID.randomUUID().toString() + "_" + fileName;
    }

    @Transactional(readOnly = true)
    public Image findById(Long imageId) {
        return imageRepository.findById(imageId).orElseThrow(() -> new RestApiException(IMAGE_NOT_FOUND));
    }
}
