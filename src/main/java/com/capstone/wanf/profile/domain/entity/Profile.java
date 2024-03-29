package com.capstone.wanf.profile.domain.entity;

import com.capstone.wanf.common.entity.BaseTimeEntity;
import com.capstone.wanf.course.domain.entity.Major;
import com.capstone.wanf.profile.dto.request.ProfileRequest;
import com.capstone.wanf.storage.domain.entity.Image;
import com.capstone.wanf.user.domain.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Entity
@Table(name = "profile")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Profile extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "student_Id")
    private int studentId;

    @Column(name = "age")
    private int age;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private MBTI mbti;

    @Enumerated(EnumType.STRING)
    @ElementCollection
    private List<Personality> personalities;

    @Enumerated(EnumType.STRING)
    @ElementCollection
    private List<Goal> goals;

    @OneToOne
    @JoinColumn(name = "major_id", referencedColumnName = "id")
    private Major major;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false, unique = true)
    private User user;

    @OneToOne
    @JoinColumn(name = "image_id")
    private Image image;

    public void updateField(ProfileRequest profileRequest) {
        this.nickname = profileRequest.nickname();

        this.age = profileRequest.age();

        this.goals = profileRequest.goals();

        this.gender = profileRequest.gender();

        this.mbti = profileRequest.mbti();

        this.studentId = profileRequest.studentId();

        this.personalities = profileRequest.personalities();
    }

    public void updateImage(Image image) {
        this.image = image;
    }

    public void updateMajor(Major major) {
        this.major = major;
    }
}
