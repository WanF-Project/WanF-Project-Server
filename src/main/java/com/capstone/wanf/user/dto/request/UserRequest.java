package com.capstone.wanf.user.dto.request;

import lombok.Data;

@Data
public class UserRequest {
    private String email;

    private String verificationCode;

    private String userPassword;
}
