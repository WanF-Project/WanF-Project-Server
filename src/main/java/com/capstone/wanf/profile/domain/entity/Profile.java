package com.capstone.wanf.profile.domain.entity;

import com.capstone.wanf.common.entity.BaseTimeEntity;
import com.capstone.wanf.course.domain.entity.Major;
import com.capstone.wanf.profile.dto.request.ProfileRequest;
import com.capstone.wanf.profile.dto.response.ProfileResponse;
import com.capstone.wanf.user.domain.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @Column(name = "profile_image")
    private String profileImage;

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
        this.nickname = profileRequest.nickname() != null ? profileRequest.nickname() : null;

        this.age = profileRequest.age() != 0 ? profileRequest.age() : 0;

        this.goals = profileRequest.goals() != null ? profileRequest.goals() : null;

        this.gender = profileRequest.gender() != null ? profileRequest.gender() : null;

        this.contact = profileRequest.contact() != null ? profileRequest.contact() : null;

        this.mbti = profileRequest.mbti() != null ? profileRequest.mbti() : null;

        this.studentId = profileRequest.studentId() != 0 ? profileRequest.studentId() : 0;

        this.personalities = profileRequest.personalities() != null ? profileRequest.personalities() : null;
    }

    public void updateMajor(Major major) {
        this.major = major;
    }

    public void updateProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public ProfileResponse toDTO() {
        return ProfileResponse.builder()
                .id(id)
                .nickname(nickname != null ? nickname : null)
                .studentId(studentId != 0 ? studentId : null)
                .age(age != 0 ? age : null)
                .contact(contact != null ? contact : null)
                .profileImage(profileImage)
                .gender(gender != null ? Map.of(gender.name(), gender.getGender()) : null)
                .mbti(mbti != null ? mbti : null)
                .personalities(personalities != null ? personalities.stream()
                        .collect(Collectors
                                .toMap(Personality::name, Personality::getDetail)) : null)
                .goals(goals != null ? goals.stream()
                        .collect(Collectors
                                .toMap(Goal::name, Goal::getDetail)) : null)
                .major(major != null ? major : null)
                .build();
    }
}
