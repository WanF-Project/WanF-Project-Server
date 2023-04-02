package com.capstone.wanf.course.domain.entity;

import com.capstone.wanf.major.domain.entity.Major;
import com.capstone.wanf.professor.domain.entity.Professor;
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
}
