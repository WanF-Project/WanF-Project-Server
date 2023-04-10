package com.capstone.wanf.post.domain.entity;

import com.capstone.wanf.course.domain.entity.Course;
import com.capstone.wanf.post.dto.request.RequestPost;
import com.capstone.wanf.profile.domain.entity.Profile;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@Table(name = "post")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false)
    private String content;

    @OneToOne
    @JoinColumn(name = "course_id", referencedColumnName = "id")
    private Course course;

    @OneToOne
    @JoinColumn(name = "profile_id", referencedColumnName = "id")
    private Profile profile;

    public void update(RequestPost requestPost) {
        this.title = requestPost.getTitle();
        this.content = requestPost.getContent();
    }
}
