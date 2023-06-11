package com.capstone.wanf.club.service;

import com.capstone.wanf.club.domain.entity.Club;
import com.capstone.wanf.club.domain.entity.ClubAuth;
import com.capstone.wanf.club.domain.repo.ClubAuthRepository;
import com.capstone.wanf.error.exception.RestApiException;
import com.capstone.wanf.user.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.capstone.wanf.error.errorcode.CommonErrorCode.FORBIDDEN;

@RequiredArgsConstructor
@Service
public class ClubAuthService {
    private final ClubAuthRepository clubAuthRepository;

    @Transactional
    public void grantAuthorityToClub(User user, Club club, String authority) {
        ClubAuth clubAuth = ClubAuth.builder()
                .user(user)
                .club(club)
                .authority(authority)
                .build();

        clubAuthRepository.save(clubAuth);
    }

    @Transactional(readOnly = true)
    public List<ClubAuth> findByUserId(Long userId) {
        List<ClubAuth> clubAuthList = clubAuthRepository.findByUserId(userId);

        return clubAuthList;
    }

    @Transactional(readOnly = true)
    public String findByUserIdAndClubId(Long userId, Long clubId) {
        ClubAuth userAuth = clubAuthRepository.findByUserIdAndClubId(userId, clubId).orElse(null);

        if (userAuth == null) return "권한 없음";

        return userAuth.getAuthority();
    }

    @Transactional(readOnly = true)
    public String getAuthority(Long userId, Long clubId) {
        String userAuth = findByUserIdAndClubId(userId, clubId);

        if (userAuth.equals("권한 없음")) throw new RestApiException(FORBIDDEN);

        else return userAuth;
    }
}
