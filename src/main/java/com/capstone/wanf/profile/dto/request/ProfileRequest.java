package com.capstone.wanf.profile.dto.request;

import com.capstone.wanf.profile.domain.entity.*;
import lombok.Data;


import java.util.List;

@Data
public class ProfileRequest {
    private ProfileImage profileImage;

    private String nickname;

    private Long majorId;

    private Integer studentId;

    private Integer age;

    private Gender gender;

    private MBTI mbti;

    private List<Personality> personalities;

    private List<Goal> goals;

    private String contact;
}
