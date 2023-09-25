package com.capstone.wanf.club.service;

import com.capstone.wanf.club.domain.entity.Club;
import com.capstone.wanf.club.domain.entity.ClubPost;
import com.capstone.wanf.club.dto.request.ClubPostRequest;
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

    private final ClubAuthService clubAuthService;

    private final ClubService clubService;

    private final S3Service s3Service;

    @Transactional
    public ClubPost save(User user, Long clubId, ClubPostRequest clubPostRequest) {
        clubAuthService.getAuthority(user.getId(), clubId);

        Club club = clubService.findById(clubId);

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
    public List<ClubPost> delete(User user, Long clubId, Long clubPostId) {
        Club club = clubService.findById(clubId);

        Profile loginUser = profileService.findByUser(user);

        ClubPost clubPost = findById(club, clubPostId);

        Profile author = clubPost.getProfile();

        if (!loginUser.getId().equals(author.getId())) {
            throw new RestApiException(NOT_POST_WRITER);
        }

        if (clubPost.getImage() != null && clubPost.getImage().getId() != 1L) {
            s3Service.delete(clubPost.getImage());
        }

        List<ClubPost> clubPosts = club.removePost(clubPostId);

        return clubPosts;
    }

    @Transactional(readOnly = true)
    public ClubPost findById(Club club, Long clubPostId) {
        ClubPost clubPost = getClubPost(clubPostId, club);

        return clubPost;
    }

    @Transactional(readOnly = true)
    public ClubPost findById(Long clubId, Long clubPostId) {
        Club club = clubService.findById(clubId);

        ClubPost clubPost = getClubPost(clubPostId, club);

        return clubPost;
    }

    @Transactional(readOnly = true)
    public List<ClubPost> findAll(Long clubId, User user) {
        clubAuthService.getAuthority(user.getId(), clubId);

        List<ClubPost> clubPosts = findAllByClubId(clubId);

        return clubPosts;
    }

    private ClubPost getClubPost(Long clubPostId, Club club) {
        ClubPost clubPost = club.getPosts().stream()
                .filter(post -> post.getId().equals(clubPostId))
                .findFirst()
                .orElseThrow(() -> new RestApiException(CLUBPOST_NOT_FOUND));
        return clubPost;
    }

    private List<ClubPost> findAllByClubId(Long clubId) {
        Club club = clubService.findById(clubId);

        List<ClubPost> clubPosts = club.getPosts().stream()
                .sorted((o1, o2) -> o2.getCreatedDate().compareTo(o1.getCreatedDate()))
                .toList();

        return clubPosts;
    }
}