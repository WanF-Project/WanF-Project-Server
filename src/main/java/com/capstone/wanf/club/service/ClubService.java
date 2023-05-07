package com.capstone.wanf.club.service;

import com.capstone.wanf.club.domain.entity.Club;
import com.capstone.wanf.club.domain.repo.ClubRepository;
import com.capstone.wanf.club.dto.request.ClubRequest;
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

    @Transactional
    public Club save(ClubRequest clubRequest) {
        Club club = Club.builder()
                .name(clubRequest.getName())
                .maxParticipants(clubRequest.getMaxParticipants())
                .currentParticipants(1)
                .password(clubRequest.getPassword())
                .recruitmentStatus(false)
                .build();

        return clubRepository.save(club);
    }
}
