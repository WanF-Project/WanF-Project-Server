package com.capstone.wanf.club.service;

import com.capstone.wanf.club.domain.entity.Club;
import com.capstone.wanf.club.domain.entity.ClubPost;
import com.capstone.wanf.club.dto.request.ClubPostRequest;
import com.capstone.wanf.common.annotation.CurrentUser;
import com.capstone.wanf.error.exception.RestApiException;
import com.capstone.wanf.profile.domain.entity.Profile;
import com.capstone.wanf.profile.service.ProfileService;
import com.capstone.wanf.storage.domain.entity.Image;
import com.capstone.wanf.storage.service.S3Service;
import com.capstone.wanf.user.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.capstone.wanf.error.errorcode.CommonErrorCode.NOT_POST_WRITER;
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

        List<ClubPost> clubPosts = club.getPosts().stream()
                .sorted((o1, o2) -> o2.getCreatedDate().compareTo(o1.getCreatedDate()))
                .toList();

        return clubPosts;
    }

    @Transactional
    public ClubPost save(@CurrentUser User user, Club club, ClubPostRequest clubPostRequest) {
        Profile userProfile = profileService.findByUser(user);

        Image image = clubPostRequest.imageId() != null ? s3Service.findById(clubPostRequest.imageId()) : null;

        ClubPost clubPost = ClubPost.builder()
                .content(clubPostRequest.content())
                .profile(userProfile)
                .image(image)
                .build();

        club.getPosts().add(clubPost);

        return clubPost;
    }

    @Transactional
    public void delete(User user, Club club, Long clubPostId) {
        Profile loginUser = profileService.findByUser(user);

        ClubPost clubPost = findById(club, clubPostId);

        Profile author = clubPost.getProfile();

        if (!loginUser.getId().equals(author.getId())) {
            throw new RestApiException(NOT_POST_WRITER);
        }

        if (clubPost.getImage() != null && clubPost.getImage().getId() != 1L) {
            s3Service.delete(clubPost.getImage());
        }

        club.removePost(clubPostId);
    }

    @Transactional(readOnly = true)
    public ClubPost findById(Club club, Long clubPostId) {
        return club.getPosts().stream()
                .filter(clubPost -> clubPost.getId() == clubPostId)
                .findFirst()
                .orElseThrow(() -> new RestApiException(CLUBPOST_NOT_FOUND));
    }
}