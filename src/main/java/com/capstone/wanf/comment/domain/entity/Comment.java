package com.capstone.wanf.comment.domain.entity;

import com.capstone.wanf.comment.dto.response.CommentResponse;
import com.capstone.wanf.common.entity.BaseTimeEntity;
import com.capstone.wanf.profile.domain.entity.Profile;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@Table(name = "comment")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Comment extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "content", nullable = false)
    private String content;

    @OneToOne
    @JoinColumn(name = "profile_id", referencedColumnName = "id")
    private Profile profile;

    public void update(String content) {
        this.content = content;
    }

    public CommentResponse toDTO() {
        return CommentResponse.builder()
                .id(this.id)
                .content(this.content)
                .profile(this.profile.toDTO())
                .build();
    }
}
