package com.capstone.wanf.banner.domain.entity;

import com.capstone.wanf.storage.domain.entity.Image;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@Table(name = "banner")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Banner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "url", nullable = false)
    private String url;

    @OneToOne
    @JoinColumn(name = "image_id")
    private Image image;
}
