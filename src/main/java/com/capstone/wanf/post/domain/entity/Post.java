package com.capstone.wanf.post.domain.entity;

import com.capstone.wanf.comment.domain.entity.Comment;
import com.capstone.wanf.common.entity.BaseTimeEntity;
import com.capstone.wanf.course.domain.entity.Course;
import com.capstone.wanf.post.dto.request.PostRequest;
import com.capstone.wanf.post.dto.response.PostPaginationResponse;
import com.capstone.wanf.post.dto.response.PostResponse;
import com.capstone.wanf.profile.domain.entity.Profile;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
@Entity
@Table(name = "post")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Post extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    private Category category;

    @OneToOne
    @JoinColumn(name = "course_id", referencedColumnName = "id",nullable = false)
    private Course course;

    @OneToOne
    @JoinColumn(name = "profile_id", referencedColumnName = "id",nullable = false)
    private Profile profile;

    @Builder.Default
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    public void update(PostRequest postRequest) {
        this.title = postRequest.title();
        this.content = postRequest.content();
    }

    public void update(Course course) {
        this.course = course;
    }

    public PostResponse toPostResponse(){
        return PostResponse.builder()
                .id(this.id)
                .title(this.title)
                .content(this.content)
                .category(Map.of(this.category.name(), this.category.getName()))
                .course(this.course)
                .profile(this.profile.toDTO())
                .createdDate(this.getCreatedDate())
                .modifiedDate(this.getModifiedDate())
                .comments(comments != null ? this.comments.stream().map(Comment::toDTO).toList() : null)
                .build();
    }

    public PostPaginationResponse toPostPaginationResponse(){
        return PostPaginationResponse.builder()
                .id(this.id)
                .course(this.course.toCoursePaginationResponse())
                .title(this.title)
                .createdDate(this.getCreatedDate())
                .modifiedDate(this.getModifiedDate())
                .build();
    }
}
