package com.capstone.wanf.club.domain.entity;

import com.capstone.wanf.club.dto.response.ClubDetailResponse;
import com.capstone.wanf.club.dto.response.ClubResponse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClubTest {
    private static final String CLUB_NAME = "clubName";

    private static final int CLUB_MAX_PARTICIPANTS = 10;

    private static final int CURRENT_PARTICIPANTS = 5;

    private static final String CLUB_PASSWORD = "clubPassword";

    private static final boolean RECRUITMENT_STATUS = false;

    @Test
    void 모임을_생성한다(){
        //when
        Club club = Club.builder()
                .name(CLUB_NAME)
                .maxParticipants(CLUB_MAX_PARTICIPANTS)
                .currentParticipants(CURRENT_PARTICIPANTS)
                .password(CLUB_PASSWORD)
                .recruitmentStatus(RECRUITMENT_STATUS)
                .build();
        //then
        assertAll(
                () -> assertEquals(CLUB_NAME, club.getName()),
                () -> assertEquals(CLUB_MAX_PARTICIPANTS, club.getMaxParticipants()),
                () -> assertEquals(CURRENT_PARTICIPANTS, club.getCurrentParticipants()),
                () -> assertEquals(CLUB_PASSWORD, club.getPassword()),
                () -> assertEquals(club.getPosts().size(), 0),
                () -> assertEquals(RECRUITMENT_STATUS, club.isRecruitmentStatus())
        );
    }

    @Test
    void 모임의_현재_참여자_수를_증가시킨다(){
        //given
        Club club = Club.builder()
                .name(CLUB_NAME)
                .maxParticipants(CLUB_MAX_PARTICIPANTS)
                .currentParticipants(CURRENT_PARTICIPANTS)
                .password(CLUB_PASSWORD)
                .recruitmentStatus(RECRUITMENT_STATUS)
                .build();
        //when
        club.updateCurrentParticipants();
        //then
        assertEquals(CURRENT_PARTICIPANTS + 1, club.getCurrentParticipants());
    }

    @Test
    void 모임의_모집_상태를_변경한다(){
        //given
        Club club = Club.builder()
                .name(CLUB_NAME)
                .maxParticipants(CLUB_MAX_PARTICIPANTS)
                .currentParticipants(CURRENT_PARTICIPANTS)
                .password(CLUB_PASSWORD)
                .recruitmentStatus(RECRUITMENT_STATUS)
                .build();
        //when
        club.updateRecruitmentStatus();
        //then
        assertTrue(club.isRecruitmentStatus());
    }

    @Test
    void 모임을_ClubResponse에_담는다(){
        //given
        Club club = Club.builder()
                .id(1L)
                .name(CLUB_NAME)
                .maxParticipants(CLUB_MAX_PARTICIPANTS)
                .currentParticipants(CURRENT_PARTICIPANTS)
                .password(CLUB_PASSWORD)
                .recruitmentStatus(RECRUITMENT_STATUS)
                .build();
        //when
        ClubResponse clubResponse = club.toResponse();
        //then
        assertAll(
                () -> assertEquals(club.getId(), clubResponse.id()),
                () -> assertEquals(club.getName(), clubResponse.name())
        );
    }

    @Test
    void 모임을_ClubDetailResponse에_담는다(){
        //given
        Club club = Club.builder()
                .id(1L)
                .name(CLUB_NAME)
                .maxParticipants(CLUB_MAX_PARTICIPANTS)
                .currentParticipants(CURRENT_PARTICIPANTS)
                .password(CLUB_PASSWORD)
                .recruitmentStatus(RECRUITMENT_STATUS)
                .build();
        //when
        ClubDetailResponse detailResponse = club.toDetailResponse();
        //then
        assertAll(
                () -> assertEquals(club.getId(), detailResponse.id()),
                () -> assertEquals(club.getName(), detailResponse.name()),
                () -> assertEquals(club.getMaxParticipants(), detailResponse.maxParticipants()),
                () -> assertEquals(club.getCurrentParticipants(), detailResponse.currentParticipants()),
                () -> assertEquals(club.isRecruitmentStatus(), detailResponse.recruitmentStatus())
        );
    }
}