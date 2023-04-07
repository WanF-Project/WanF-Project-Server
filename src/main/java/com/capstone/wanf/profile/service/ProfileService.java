package com.capstone.wanf.profile.service;

import com.capstone.wanf.major.domain.entity.Major;
import com.capstone.wanf.major.service.MajorService;
import com.capstone.wanf.profile.domain.entity.Profile;
import com.capstone.wanf.profile.domain.repo.ProfileRepository;
import com.capstone.wanf.profile.dto.request.ProfileRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final ProfileRepository profileRepository;

    private final MajorService majorService;

    @Transactional(readOnly = true)
    public Profile findById(Long id) {
        Profile profile = profileRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("프로필이 존재하지 않습니다."));

        return profile;
    }

    @Transactional
    public Profile save(ProfileRequest profileRequest) {
        Major major = majorService.findById(profileRequest.getMajorId());

        Profile saveProfile = Profile.builder()
                .profileImage(profileRequest.getProfileImage())
                .nickname(profileRequest.getNickname())
                .major(major)
                .studentId(profileRequest.getStudentId())
                .age(profileRequest.getAge())
                .gender(profileRequest.getGender())
                .mbti(profileRequest.getMbti())
                .personalities(profileRequest.getPersonalities())
                .goals(profileRequest.getGoals())
                .contact(profileRequest.getContact())
                .build();

        Profile profile = profileRepository.save(saveProfile);

        return profile;
    }


    public Profile update(Long id, ProfileRequest profileRequest) {
        Profile profile = profileRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("프로필이 존재하지 않습니다."));

        profile.update(profileRequest);

        Profile updateProfile = profileRepository.save(profile);

        return updateProfile;
    }
}
