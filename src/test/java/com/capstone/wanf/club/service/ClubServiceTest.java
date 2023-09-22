package com.capstone.wanf.club.service;

import com.capstone.wanf.club.domain.entity.Authority;
import com.capstone.wanf.club.domain.entity.Club;
import com.capstone.wanf.club.domain.repo.ClubRepository;
import com.capstone.wanf.club.dto.request.ClubPwdRequest;
import com.capstone.wanf.error.exception.RestApiException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.capstone.wanf.fixture.DomainFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class ClubServiceTest {
    @InjectMocks
    private ClubService clubService;

    @Mock
    private ClubRepository clubRepository;

    @Mock
    private ClubAuthService clubAuthService;

    @Test
    void 모임을_모두_조회한다(){
        //given
        given(clubRepository.findAll()).willReturn(List.of(모임1, 모임2));
        //when
        List<Club> clubs = clubService.findAll();
        //then
        assertThat(clubs.size()).isEqualTo(2);
    }

    @Test
    void ID에_해당하는_모임을_조회한다(){
        //given
        given(clubRepository.findById(any())).willReturn(Optional.of(모임1));
        //when
        Club club = clubService.findById(모임1.getId());
        //then
        assertThat(club.getId()).isEqualTo(모임1.getId());
    }

    @Test
    void 해당하는_모임이_존재하지_않으면_예외를_던진다(){
        //given
        given(clubRepository.findById(any())).willReturn(Optional.empty());
        //when & then
        assertThatThrownBy(() -> clubService.findById(모임1.getId()))
                .isInstanceOf(RestApiException.class);
    }

    @Test
    void 모임에_가입한다(){
        //given
        given(clubRepository.findById(any())).willReturn(Optional.of(모임1));

        given(clubAuthService.findByUserIdAndClubId(any(), any())).willReturn(Authority.NONE);
        //when
        clubService.joinClub(유저1, 모임_비밀번호_요청1);
        //then
        then(clubAuthService).should(times(1)).grantAuthorityToClub(유저1, 모임1, Authority.CLUB_MEMBER);
    }

    @Test
    void 이미_해당_모임에_가입되어_있으면_예외를_던진다(){
        //given
        given(clubRepository.findById(any())).willReturn(Optional.of(모임1));

        given(clubAuthService.findByUserIdAndClubId(any(), any())).willReturn(Authority.CLUB_LEADER);
        //when & then
        assertThatThrownBy(() -> clubService.joinClub(유저1, 모임_비밀번호_요청1))
                .isInstanceOf(RestApiException.class);
    }

    @Test
    void 해당_모임의_인원이_다_찼을_경우_예외를_던진다(){
        //given
        given(clubRepository.findById(any())).willReturn(Optional.of(모임3));

        given(clubAuthService.findByUserIdAndClubId(any(), any())).willReturn(Authority.NONE);
        //when & then
        assertThatThrownBy(() -> clubService.joinClub(유저1, 모임_비밀번호_요청1))
                .isInstanceOf(RestApiException.class);
    }

    @Test
    void 비밀번호가_틀리다면_예외를_던진다(){
        //given
        given(clubRepository.findById(any())).willReturn(Optional.of(모임2));

        given(clubAuthService.findByUserIdAndClubId(any(), any())).willReturn(Authority.NONE);
        //when & then
        assertThatThrownBy(() -> clubService.joinClub(유저1, 모임_비밀번호_요청1))
                .isInstanceOf(RestApiException.class);
    }

    @Test
    void 모임을_생성한다(){
        //given
        given(clubRepository.save(any())).willReturn(모임1);
        //when
        Club club = clubService.save(모임_요청1, 유저1);
        //then
        assertThat(club.getId()).isEqualTo(모임1.getId());
    }

    @Test
    void 모임_권한을_확인한다(){
        //given
        given(clubRepository.findById(any())).willReturn(Optional.of(모임1));

        given(clubAuthService.getAuthority(any(), any())).willReturn(Authority.CLUB_LEADER);
        //when
        Authority authority = clubService.checkAuthority(모임1.getId(), 유저1);
        //then
        assertThat(authority).isEqualTo(Authority.CLUB_LEADER);
    }

    @Test
    void 모임_비밀번호를_조회한다(){
        //given
        given(clubRepository.findById(any())).willReturn(Optional.of(모임1));
        //when
        ClubPwdRequest password = clubService.getPassword(모임1.getId());
        //then
        assertThat(password.password()).isEqualTo(모임1.getPassword());
    }
}