package com.capstone.wanf.club.service;

import com.capstone.wanf.club.domain.entity.Authority;
import com.capstone.wanf.club.domain.entity.Club;
import com.capstone.wanf.club.domain.repo.ClubRepository;
import com.capstone.wanf.club.dto.request.ClubPwdRequest;
import com.capstone.wanf.club.dto.request.ClubRequest;
import com.capstone.wanf.error.exception.RestApiException;
import com.capstone.wanf.user.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.capstone.wanf.error.errorcode.CommonErrorCode.*;
import static com.capstone.wanf.error.errorcode.CustomErrorCode.CLUB_NOT_FOUND;
import static com.capstone.wanf.error.errorcode.CustomErrorCode.CLUB_UNAUTHORIZED;

@Service
@RequiredArgsConstructor
public class ClubService {
    private final ClubRepository clubRepository;

    private final ClubAuthService clubAuthService;

    @Transactional(readOnly = true)
    public List<Club> findAll() {
        List<Club> clubs = clubRepository.findAll();

        return clubs;
    }

    @Transactional(readOnly = true)
    public Club findById(Long id) {
        Club club = clubRepository.findById(id).orElseThrow(() -> new RestApiException(CLUB_NOT_FOUND));

        return club;
    }

    @Transactional
    public void joinClub(User user, ClubPwdRequest clubPwdRequest) {
        Club club = findById(clubPwdRequest.clubId());

        Authority userAuth = clubAuthService.findByUserIdAndClubId(user.getId(), club.getId());

        if (checkClubAccess(club, clubPwdRequest, userAuth)) {
            clubAuthService.grantAuthorityToClub(user, club, Authority.CLUB_MEMBER);
        }
    }

    @Transactional
    public Club save(ClubRequest clubRequest, User user) {
        Club club = Club.builder()
                .name(clubRequest.name())
                .maxParticipants(clubRequest.maxParticipants())
                .currentParticipants(1)
                .password(clubRequest.password())
                .recruitmentStatus(false)
                .build();

        if (clubRequest.maxParticipants() == 1) club.updateRecruitmentStatus();

        Club saveClub = clubRepository.save(club);

        clubAuthService.grantAuthorityToClub(user, club, Authority.CLUB_LEADER);

        return saveClub;
    }

    @Transactional(readOnly = true)
    public Authority checkAuthority(Long clubId, User user) {
        Club club = findById(clubId);

        Authority userAuth = clubAuthService.getAuthority(user.getId(), club.getId());

        return userAuth;
    }

    @Transactional(readOnly = true)
    public ClubPwdRequest getPassword(Long clubId) {
        Club club = findById(clubId);

        ClubPwdRequest clubPwdRequest = ClubPwdRequest.builder()
                .clubId(clubId)
                .password(club.getPassword())
                .build();

        return clubPwdRequest;
    }

    private Boolean checkClubAccess(Club club, ClubPwdRequest clubPwdRequest, Authority userAuth) {
        checkRecruitmentStatus(club);

        checkUserAuthority(userAuth);

        validatePassword(club, clubPwdRequest);

        updateRecruitmentStatusIfFull(club);

        return true;
    }

    private void validatePassword(Club club, ClubPwdRequest request) {
        if (!request.password().equals(club.getPassword())) {
            throw new RestApiException(CLUB_UNAUTHORIZED);
        }
    }

    private void checkUserAuthority(Authority userAuth) {
        if (userAuth != Authority.NONE) {
            throw new RestApiException(ALREADY_JOIN_CLUB);
        }
    }

    private void checkRecruitmentStatus(Club club) {
        if (club.isRecruitmentStatus()) {
            throw new RestApiException(FULL_CLUB);
        }
    }
    
    private void updateRecruitmentStatusIfFull(Club club) {
        club.updateCurrentParticipants();

        if (club.getMaxParticipants() == club.getCurrentParticipants()) {
            club.updateRecruitmentStatus();
        }
    }
}