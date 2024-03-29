package com.capstone.wanf.club.domain.entity;

import com.capstone.wanf.common.entity.BaseTimeEntity;
import com.capstone.wanf.profile.domain.entity.Profile;
import com.capstone.wanf.storage.domain.entity.Image;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Table(name = "club_post")
@Entity
public class ClubPost extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "content", nullable = false)
    private String content;

    @OneToOne
    @JoinColumn(name = "image_id")
    private Image image;

    @OneToOne
    @JoinColumn(name = "profile_id")
    private Profile profile;
}