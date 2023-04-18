package com.capstone.wanf.comment.domain.entity;

import com.capstone.wanf.common.entity.BaseTimeEntity;
import com.capstone.wanf.post.domain.entity.Post;
import com.capstone.wanf.profile.domain.entity.Profile;
import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "post_id", referencedColumnName = "id")
    private Post post;

    public void update(String content) {
        this.content = content;
    }
}
