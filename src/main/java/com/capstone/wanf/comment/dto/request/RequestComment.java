package com.capstone.wanf.comment.dto.request;

import lombok.Data;

@Data
public class RequestComment {
    private String content;

    // TODO: 2023-04-14 나중에 로그인 구현 완성되면 수정
    private Long profileId;
}
