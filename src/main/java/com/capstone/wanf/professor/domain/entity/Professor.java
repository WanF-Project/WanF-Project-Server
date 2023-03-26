package com.capstone.wanf.professor.domain.entity;

import com.capstone.wanf.major.domain.entity.Major;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@Table(name = "professor")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Professor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "major_id", referencedColumnName = "id")
    private Major major;
}
