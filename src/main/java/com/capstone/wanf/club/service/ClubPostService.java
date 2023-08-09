package com.capstone.wanf.club.service;

import com.capstone.wanf.club.domain.entity.Club;
import com.capstone.wanf.club.domain.entity.ClubPost;
import com.capstone.wanf.club.dto.request.ClubPostRequest;
import com.capstone.wanf.common.annotation.CurrentUser;
import com.capstone.wanf.error.exception.RestApiException;
import com.capstone.wanf.profile.domain.entity.Profile;
import com.capstone.wanf.profile.service.ProfileService;
import com.capstone.wanf.storage.service.S3Service;
import com.capstone.wanf.user.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.capstone.wanf.error.errorcode.CommonErrorCode.FORBIDDEN;
import static com.capstone.wanf.error.errorcode.CustomErrorCode.CLUBPOST_NOT_FOUND;

@RequiredArgsConstructor
@Service
public class ClubPostService {
    private final ProfileService profileService;

    private final ClubService clubService;

    private final S3Service s3Service;

    @Transactional(readOnly = true)
    public List<ClubPost> findAllByClubId(Long clubId) {
        Club club = clubService.findById(clubId);

        // 최신순 정렬
        List<ClubPost> clubPosts = club.getPosts().stream()
                .sorted((o1, o2) -> o2.getCreatedDate().compareTo(o1.getCreatedDate()))
                .toList();

        return clubPosts;
    }

    @Transactional
    public ClubPost save(@CurrentUser User user, Club club, ClubPostRequest clubPostRequest) {
        Profile userProfile = profileService.findByUser(user);

        MultipartFile image = clubPostRequest.image();

        ClubPost clubPost = ClubPost.builder()
                .content(clubPostRequest.content())
                .profile(userProfile)
                .imageUrl(image.isEmpty() ? null : s3Service.upload(image, "images"))
                .build();

        club.getPosts().add(clubPost);

        return clubPost;
    }

    @Transactional
    public void delete(User user, Long clubId, Long clubPostId) {
        Profile loginUser = profileService.findByUser(user);

        Profile author = findById(clubId, clubPostId).getProfile();

        if (loginUser.getId() == author.getId()) {
            clubService.findById(clubId).getPosts().removeIf(clubPost -> clubPost.getId() == clubPostId);
        } else throw new RestApiException(FORBIDDEN);
    }

    @Transactional(readOnly = true)
    public ClubPost findById(Long clubId, Long clubPostId) {
        Club club = clubService.findById(clubId);

        return club.getPosts().stream()
                .filter(clubPost -> clubPost.getId() == clubPostId)
                .findFirst()
                .orElseThrow(() -> new RestApiException(CLUBPOST_NOT_FOUND));
    }
}