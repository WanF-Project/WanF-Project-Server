package com.capstone.wanf.club.service;

import com.capstone.wanf.club.domain.entity.Club;
import com.capstone.wanf.club.domain.entity.ClubAuth;
import com.capstone.wanf.club.domain.repo.ClubAuthRepository;
import com.capstone.wanf.user.domain.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}
