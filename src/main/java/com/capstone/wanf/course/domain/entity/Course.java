package com.capstone.wanf.course.domain.entity;

import com.capstone.wanf.course.dto.response.CoursePaginationResponse;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@Table(name = "course")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "course_id",nullable = false)
    private String courseId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "professor")
    private String professor;

    @Column(name = "course_time")
    private String courseTime;

    @Column(name = "category")
    private String category;

    @OneToOne
    @JoinColumn(name = "major_id", referencedColumnName = "id")
    private Major major;

    public CoursePaginationResponse toCoursePaginationResponse(){
        return CoursePaginationResponse.builder()
                .id(this.id)
                .name(this.name)
                .professor(this.professor)
                .build();
    }
}
