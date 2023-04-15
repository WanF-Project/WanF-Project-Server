package com.capstone.wanf.post.domain.entity;

import lombok.Getter;

@Getter
public enum Category {
    friend("수업 친구"), course("수업");

    private final String name;

    Category(String name) {
        this.name = name;
    }
}
