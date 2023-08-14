package com.capstone.wanf.storage.domain.entity;

import lombok.Getter;

@Getter
public enum Directory {
    profiles("profiles"), posts("posts");

    private final String name;

    Directory(String name) {
        this.name = name;
    }
}
