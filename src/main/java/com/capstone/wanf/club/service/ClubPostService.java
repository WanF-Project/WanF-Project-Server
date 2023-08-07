package com.capstone.wanf.club.service;

import com.capstone.wanf.club.domain.entity.Club;
import com.capstone.wanf.club.domain.entity.ClubPost;
import com.capstone.wanf.club.domain.repo.ClubPostRepository;
import com.capstone.wanf.club.dto.request.ClubPostRequest;
import com.capstone.wanf.error.exception.RestApiException;
import com.capstone.wanf.profile.service.ProfileService;
import com.capstone.wanf.user.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.capstone.wanf.error.errorcode.CommonErrorCode.FORBIDDEN;
import static com.capstone.wanf.error.errorcode.CustomErrorCode.CLUBPOST_NOT_FOUND;

@RequiredArgsConstructor
@Service
public class ClubPostService {
    private final ClubPostRepository clubPostRepository;

    private final ProfileService profileService;

    @Transactional(readOnly = true)
    public List<ClubPost> findAllByClubId(Long clubId) {
        List<ClubPost> clubPosts = clubPostRepository.findAllByClubId(clubId);

        return clubPosts;
    }

    @Transactional
    public ClubPost save(User user, Club club, ClubPostRequest clubPostRequest) {
        ClubPost clubPost = ClubPost.builder()
                .content(clubPostRequest.content())
                .nickname(profileService.findByUser(user).getNickname())
                .user(user)
                .club(club)
                .build();

        return clubPostRepository.save(clubPost);
    }

    @Transactional
    public void delete(User loginUser, Long clubPostId) {
        User author = findById(clubPostId).getUser();

        if (loginUser.getId() == author.getId()) clubPostRepository.deleteById(clubPostId);
        else throw new RestApiException(FORBIDDEN);
    }

    @Transactional(readOnly = true)
    public ClubPost findById(Long clubPostId) {
        return clubPostRepository.findById(clubPostId).orElseThrow(() -> new RestApiException(CLUBPOST_NOT_FOUND));
    }
}