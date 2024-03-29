package com.capstone.wanf.profile.service;

import com.capstone.wanf.course.domain.entity.Major;
import com.capstone.wanf.course.service.MajorService;
import com.capstone.wanf.error.exception.RestApiException;
import com.capstone.wanf.profile.domain.entity.Goal;
import com.capstone.wanf.profile.domain.entity.MBTI;
import com.capstone.wanf.profile.domain.entity.Personality;
import com.capstone.wanf.profile.domain.entity.Profile;
import com.capstone.wanf.profile.domain.repo.ProfileRepository;
import com.capstone.wanf.profile.domain.repo.ProfileRepositorySupport;
import com.capstone.wanf.profile.dto.request.ProfileImageRequest;
import com.capstone.wanf.profile.dto.request.ProfileRequest;
import com.capstone.wanf.profile.dto.response.ProfileResponse;
import com.capstone.wanf.storage.domain.entity.Image;
import com.capstone.wanf.storage.service.S3Service;
import com.capstone.wanf.user.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
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

    private final ProfileRepositorySupport profileRepositorySupport;

    private final MajorService majorService;

    private final S3Service s3Service;

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

    @Transactional(readOnly = true)
    public Slice<ProfileResponse> findByRandom(User user, Pageable pageable) {
        Profile profile = findByUser(user);

        return profileRepositorySupport.findByRandom(pageable, profile.getId());
    }

    @Transactional
    public Profile save(ProfileImageRequest profileImageRequest, User user) {
        ProfileRequest profileRequest = profileImageRequest.profileRequest();

        Major major = majorService.findById(profileRequest.majorId());

        Image image = s3Service.findById(profileImageRequest.imageId());

        Profile profile = Profile.builder()
                .user(user)
                .nickname(profileRequest.nickname())
                .gender(profileRequest.gender())
                .age(profileRequest.age())
                .mbti(profileRequest.mbti())
                .image(image)
                .studentId(profileRequest.studentId())
                .major(major)
                .personalities(profileRequest.personalities())
                .goals(profileRequest.goals())
                .build();

        Profile saveProfile = profileRepository.save(profile);

        return saveProfile;
    }

    @Transactional
    public Profile update(User user, ProfileImageRequest profileImageRequest) {
        ProfileRequest profileRequest = profileImageRequest.profileRequest();

        Profile profile = profileRepository.findByUser(user)
                .orElseThrow(() -> new RestApiException(PROFILE_NOT_FOUND));

        Image userImage = profile.getImage();

        if (!userImage.getId().equals(profileImageRequest.imageId())) {
            Image changeImage = s3Service.findById(profileImageRequest.imageId());

            s3Service.delete(userImage);

            profile.updateImage(changeImage);
        }

        profile.updateField(profileImageRequest.profileRequest());

        if (profileRequest.majorId() != null) {
            Major major = majorService.findById(profileRequest.majorId());

            profile.updateMajor(major);
        }

        return profile;
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

    public List<MBTI> getMBTI() {
        List<MBTI> mbtiList = Arrays.asList(MBTI.values());

        return mbtiList;
    }
}
