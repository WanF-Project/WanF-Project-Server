package com.capstone.wanf.message.domain.entity;

import com.capstone.wanf.common.entity.BaseTimeEntity;
import com.capstone.wanf.user.domain.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@Table(name = "message")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Message extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private User receiver;

    @Column(columnDefinition = "TEXT")
    private String content;
}
