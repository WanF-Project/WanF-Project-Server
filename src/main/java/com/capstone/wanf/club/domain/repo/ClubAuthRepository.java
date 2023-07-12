package com.capstone.wanf.club.domain.repo;

import com.capstone.wanf.club.domain.entity.ClubAuth;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ClubAuthRepository extends JpaRepository<ClubAuth, Long> {
    List<ClubAuth> findByUserId(Long userId);

    Optional<ClubAuth> findByUserIdAndClubId(Long userId, Long clubId);
}