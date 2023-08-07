package com.capstone.wanf.club.service;

import com.capstone.wanf.club.domain.entity.Authority;
import com.capstone.wanf.club.domain.entity.Club;
import com.capstone.wanf.club.domain.repo.ClubRepository;
import com.capstone.wanf.club.dto.request.ClubPwdRequest;
import com.capstone.wanf.club.dto.request.ClubRequest;
import com.capstone.wanf.error.exception.RestApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.capstone.wanf.error.errorcode.CommonErrorCode.FORBIDDEN;
import static com.capstone.wanf.error.errorcode.CustomErrorCode.CLUB_NOT_FOUND;
import static com.capstone.wanf.error.errorcode.CustomErrorCode.CLUB_UNAUTHORIZED;

@RequiredArgsConstructor
@Service
public class ClubService {
    private final ClubRepository clubRepository;

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
    public Club save(ClubRequest clubRequest) {
        Club club = Club.builder()
                .name(clubRequest.name())
                .maxParticipants(clubRequest.maxParticipants())
                .currentParticipants(1)
                .password(clubRequest.password())
                .recruitmentStatus(false)
                .build();

        // 방장이 1인 모임을 개설할 경우
        if (clubRequest.maxParticipants() == 1) club.updateRecruitmentStatus();

        return clubRepository.save(club);
    }

    @Transactional
    public Boolean checkClubAccess(Long clubId, ClubPwdRequest clubPwdRequest, Authority userAuth) {
        Club club = findById(clubId);

        if (club.isRecruitmentStatus() | !userAuth.equals("권한 없음")) throw new RestApiException(FORBIDDEN);

        if (clubPwdRequest.password().equals(club.getPassword())) {
            club.updateCurrentParticipants();

            checkRecruitmentStatus(club);

            return true;
        } else throw new RestApiException(CLUB_UNAUTHORIZED);
    }

    @Transactional
    public void checkRecruitmentStatus(Club club) {
        if (club.getMaxParticipants() == club.getCurrentParticipants()) {
            club.updateRecruitmentStatus();
        }
    }
}