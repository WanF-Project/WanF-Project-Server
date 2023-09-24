package com.capstone.wanf.club.service;

import com.capstone.wanf.club.domain.entity.Authority;
import com.capstone.wanf.club.domain.entity.Club;
import com.capstone.wanf.club.domain.entity.ClubAuth;
import com.capstone.wanf.club.domain.repo.ClubAuthRepository;
import com.capstone.wanf.error.exception.RestApiException;
import com.capstone.wanf.user.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.capstone.wanf.error.errorcode.CommonErrorCode.CLUB_FORBIDDEN;

@Service
@RequiredArgsConstructor
public class ClubAuthService {
    private final ClubAuthRepository clubAuthRepository;

    @Transactional
    public void grantAuthorityToClub(User user, Club club, Authority authority) {
        ClubAuth clubAuth = ClubAuth.builder()
                .user(user)
                .club(club)
                .authority(authority)
                .build();

        clubAuthRepository.save(clubAuth);
    }

    @Transactional(readOnly = true)
    public List<Club> findByUserId(Long userId) {
        List<Club> clubResponses = clubAuthRepository.findByUserId(userId).stream()
                .map(ClubAuth::getClub)
                .collect(Collectors.toList());

        return clubResponses;
    }

    @Transactional(readOnly = true)
    public Authority findByUserIdAndClubId(Long userId, Long clubId) {
        ClubAuth userAuth = clubAuthRepository.findByUserIdAndClubId(userId, clubId).orElse(null);

        if (userAuth == null) return Authority.NONE;

        return userAuth.getAuthority();
    }

    @Transactional(readOnly = true)
    public Authority getAuthority(Long userId, Long clubId) {
        Authority userAuth = findByUserIdAndClubId(userId, clubId);

        if (userAuth == Authority.NONE) throw new RestApiException(CLUB_FORBIDDEN);

        else return userAuth;
    }
}