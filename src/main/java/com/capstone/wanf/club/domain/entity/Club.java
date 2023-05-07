package com.capstone.wanf.club.domain.entity;

import com.capstone.wanf.common.entity.BaseTimeEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.validator.constraints.Range;

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

    @Column(name = "user_count", nullable = false)
    @Range(min = 1, max = 5)
    private int userCount;

    @JsonIgnore
    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "recruitment_status", nullable = false)
    private boolean recruitmentStatus;
}