package com.capstone.wanf.club.service;

import com.capstone.wanf.club.domain.entity.Authority;
import com.capstone.wanf.club.domain.entity.Club;
import com.capstone.wanf.club.domain.entity.ClubAuth;
import com.capstone.wanf.club.domain.repo.ClubAuthRepository;
import com.capstone.wanf.error.exception.RestApiException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.capstone.wanf.fixture.DomainFixture.*;
import static com.capstone.wanf.fixture.DomainFixture.유저1;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class ClubServiceAuthTest {
    @InjectMocks
    private ClubAuthService clubAuthService;

    @Mock
    private ClubAuthRepository clubAuthRepository;

    @Test
    void 모임_권한을_저장한다(){
        //when
        clubAuthService.grantAuthorityToClub(유저1, 모임1, Authority.CLUB_LEADER);
        //then
        then(clubAuthRepository).should(times(1)).save(any(ClubAuth.class));
    }

    @Test
    void 모임_권한을_검색한다(){
        //given
        given(clubAuthRepository.findByUserId(any())).willReturn(List.of(리더모임권한1));
        //when
        List<Club> 모임_리스트 = clubAuthService.findByUserId(유저1.getId());
        //then
        assertThat(모임_리스트).isEqualTo(List.of(모임1));
    }

    @Test
    void 해당_모임의_권한이_없는_유저(){
        //given
        given(clubAuthRepository.findByUserIdAndClubId(any(), any())).willReturn(Optional.empty());
        //when
        Authority 유저권한 = clubAuthService.findByUserIdAndClubId(유저1.getId(), 모임1.getId());
        //then
        assertThat(유저권한).isEqualTo(Authority.NONE);
    }

    @Test
    void 유저의_해당_모임_권한_확인(){
        //given
        given(clubAuthRepository.findByUserIdAndClubId(any(), any())).willReturn(Optional.of(리더모임권한1));
        //when
        Authority 유저권한 = clubAuthService.findByUserIdAndClubId(유저1.getId(), 모임1.getId());
        //then
        assertThat(유저권한).isEqualTo(Authority.CLUB_LEADER);
    }

    @Test
    void 모임에_대한_권한이_없으면_예외가_발생한다(){
        //given
        given(clubAuthRepository.findByUserIdAndClubId(any(), any())).willReturn(Optional.empty());
        //when & then
        assertThatThrownBy(() -> clubAuthService.getAuthority(유저1.getId(), 모임1.getId()))
                .isInstanceOf(RestApiException.class);
    }

    @Test
    void 모임에_대한_사용자의_권한을_반환한다(){
        //given
        given(clubAuthRepository.findByUserIdAndClubId(any(), any())).willReturn(Optional.of(리더모임권한1));
        //when
        Authority 유저권한 = clubAuthService.getAuthority(유저1.getId(), 모임1.getId());
        //then
        assertThat(유저권한).isEqualTo(Authority.CLUB_LEADER);
    }
}