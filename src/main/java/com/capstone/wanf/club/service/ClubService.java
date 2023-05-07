package com.capstone.wanf.club.service;

import com.capstone.wanf.club.domain.entity.Club;
import com.capstone.wanf.club.domain.repo.ClubRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ClubService {
    private final ClubRepository clubRepository;

    @Transactional(readOnly = true)
    public List<Club> findAll() {
        List<Club> clubs = clubRepository.findAll();

        return clubs;
    }
}
