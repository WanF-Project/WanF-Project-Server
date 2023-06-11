package com.capstone.wanf.club.domain.repo;

import com.capstone.wanf.club.domain.entity.ClubPost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ClubPostRepository extends JpaRepository<ClubPost, Long> {
    List<ClubPost> findAllByClubId(Long clubId);

    Optional<ClubPost> findByUserId(Long UserId);
}
