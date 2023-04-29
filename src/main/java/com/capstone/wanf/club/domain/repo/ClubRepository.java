package com.capstone.wanf.club.domain.repo;

import com.capstone.wanf.club.domain.entity.Club;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClubRepository extends JpaRepository<Club, Long> {
    List<Club> findAll();
}
