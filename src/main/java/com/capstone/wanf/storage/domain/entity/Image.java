package com.capstone.wanf.storage.domain.entity;

import com.capstone.wanf.common.entity.BaseTimeEntity;
import com.capstone.wanf.storage.dto.response.ImageResponse;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Table(name = "image")
@Entity
public class Image extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    @Column(name = "directory", nullable = false)
    private Directory directory;

    @Column(name = "convert_image_name", nullable = false)
    private String convertImageName;

    public ImageResponse toResponse() {
        return ImageResponse.builder()
                .imageId(this.id)
                .imageUrl(this.imageUrl)
                .build();
    }
}
