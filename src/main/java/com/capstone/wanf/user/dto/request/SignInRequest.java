package com.capstone.wanf.user.dto.request;

import lombok.Data;

@Data
public class SignInRequest {
    private String email;
    
    private String password;
}
