package com.capstone.wanf.club.domain.entity;

import com.capstone.wanf.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.validator.constraints.Range;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Table(name = "club")
@Entity
public class Club extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "max_participants", nullable = false)
    @Range(min = 1, max = 5)
    private int maxParticipants;

    @Column(name = "current_participants", nullable = false)
    private int currentParticipants;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "recruitment_status", nullable = false)
    private boolean recruitmentStatus;

    @Builder.Default
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ClubPost> posts = new ArrayList<>();

    public void updateCurrentParticipants() {
        this.currentParticipants += 1;
    }

    public void updateRecruitmentStatus() {
        this.recruitmentStatus = true;
    }

    public List<ClubPost> removePost(Long postId) {
        this.posts.removeIf(post -> post.getId().equals(postId));

        return this.posts;
    }
}