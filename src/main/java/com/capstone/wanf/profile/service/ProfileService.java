package com.capstone.wanf.profile.service;

import com.capstone.wanf.error.exception.RestApiException;
import com.capstone.wanf.course.domain.entity.Major;
import com.capstone.wanf.course.service.MajorService;
import com.capstone.wanf.profile.domain.entity.Goal;
import com.capstone.wanf.profile.domain.entity.Personality;
import com.capstone.wanf.profile.domain.entity.Profile;
import com.capstone.wanf.profile.domain.repo.ProfileRepository;
import com.capstone.wanf.profile.dto.request.ProfileRequest;
import com.capstone.wanf.user.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.capstone.wanf.error.errorcode.CustomErrorCode.PROFILE_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final ProfileRepository profileRepository;

    private final MajorService majorService;

    @Transactional(readOnly = true)
    public Profile findById(Long id) {
        Profile profile = profileRepository.findById(id)
                .orElseThrow(() -> new RestApiException(PROFILE_NOT_FOUND));

        return profile;
    }

    @Transactional(readOnly = true)
    public Profile findByUser(User user) {
        Profile profile = profileRepository.findByUser(user)
                .orElseThrow(() -> new RestApiException(PROFILE_NOT_FOUND));

        return profile;
    }

    @Transactional
    public Profile defaultSave(User user) {
        Profile defaultProfile = Profile.builder()
                .user(user)
                .build();

        Profile profile = profileRepository.save(defaultProfile);

        return profile;
    }
    
    public Profile update(User user, ProfileRequest profileRequest) {
        Profile profile = profileRepository.findByUser(user)
                .orElseThrow(() -> new RestApiException(PROFILE_NOT_FOUND));

        profile.updateField(profileRequest);

        if(profileRequest.majorId() != null) {
            Major major = majorService.findById(profileRequest.majorId());

            profile.updateMajor(major);
        }

        Profile updateProfile = profileRepository.save(profile);

        return updateProfile;
    }

    public Map<String, String> getPersonalities() {
        List<Personality> personalityList = Arrays.asList(Personality.values());

        Map<String, String> personalities = personalityList.stream()
                .collect(Collectors.toMap(Personality::name, Personality::getDetail));

        return personalities;
    }

    public Map<String, String> getGoals() {
        List<Goal> goalList = Arrays.asList(Goal.values());

        Map<String, String> goals = goalList.stream()
                .collect(Collectors.toMap(Goal::name, Goal::getDetail));

        return goals;
    }

    @Transactional
    public Profile save(ProfileRequest profileRequest, User user) {
        Major major = majorService.findById(profileRequest.majorId());

        Profile profile = Profile.builder()
                .user(user)
                .nickname(profileRequest.nickname())
                .gender(profileRequest.gender())
                .age(profileRequest.age())
                .contact(profileRequest.contact())
                .mbti(profileRequest.mbti())
                .profileImage(profileRequest.profileImage())
                .studentId(profileRequest.studentId())
                .major(major)
                .personalities(profileRequest.personalities())
                .goals(profileRequest.goals())
                .build();

        Profile saveProfile = profileRepository.save(profile);

        return saveProfile;
    }
}
