package com.capstone.wanf.club.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.validator.constraints.Range;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Table(name = "club")
@Entity
public class Club {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "user_count", nullable = false)
    @Range(min = 1, max = 5)
    private int userCount;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "recruitment_status", nullable = false)
    private boolean recruitmentStatus;
}