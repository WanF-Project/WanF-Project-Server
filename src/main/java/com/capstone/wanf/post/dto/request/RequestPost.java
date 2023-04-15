package com.capstone.wanf.post.dto.request;

import lombok.Data;

@Data
public class RequestPost {
    private String title;

    private String content;

    private Long courseId;

    // TODO: 2023/04/10 나중에 로그인 구현 완성되면 수정
    private Long profileId;
}
