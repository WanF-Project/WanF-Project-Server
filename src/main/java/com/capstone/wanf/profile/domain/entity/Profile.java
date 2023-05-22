package com.capstone.wanf.profile.domain.entity;

import com.capstone.wanf.common.entity.BaseTimeEntity;
import com.capstone.wanf.course.domain.entity.Major;
import com.capstone.wanf.profile.dto.request.ProfileRequest;
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

    @Column(name = "contact", columnDefinition = "TEXT")
    private String contact;

    @Enumerated(EnumType.STRING)
    private ProfileImage profileImage;

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

    public void updateField(ProfileRequest profileRequest) {
        this.profileImage = profileRequest.getProfileImage() != null ? profileRequest.getProfileImage() : null;

        this.nickname = profileRequest.getNickname() != null ? profileRequest.getNickname() : null;

        this.age = profileRequest.getAge() != 0 ? profileRequest.getAge() : 0;

        this.goals = profileRequest.getGoals() != null ? profileRequest.getGoals() : null;

        this.gender = profileRequest.getGender() != null ? profileRequest.getGender() : null;

        this.contact = profileRequest.getContact() != null ? profileRequest.getContact() : null;

        this.mbti = profileRequest.getMbti() != null ? profileRequest.getMbti() : null;

        this.studentId = profileRequest.getStudentId() != 0 ? profileRequest.getStudentId() : 0;

        this.personalities = profileRequest.getPersonalities() != null ? profileRequest.getPersonalities() : null;
    }

    public void updateMajor(Major major) {
        this.major = major;
    }
}
