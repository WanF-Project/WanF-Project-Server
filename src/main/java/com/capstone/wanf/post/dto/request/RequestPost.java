package com.capstone.wanf.post.dto.request;

import lombok.Data;

@Data
public class RequestPost {
    private String title;

    private String content;

    private Long courseId;
}
