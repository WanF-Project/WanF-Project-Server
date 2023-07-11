package com.capstone.wanf.post.domain.entity;

import lombok.Getter;

@Getter
public enum Category {
    friend("강의 친구"), course("강의 후기");

    private final String name;

    Category(String name) {
        this.name = name;
    }
}
