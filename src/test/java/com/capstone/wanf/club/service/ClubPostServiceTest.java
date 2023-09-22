package com.capstone.wanf.club.service;

import com.capstone.wanf.club.domain.entity.ClubPost;
import com.capstone.wanf.club.domain.repo.ClubAuthRepository;
import com.capstone.wanf.club.dto.response.ClubPostResponse;
import com.capstone.wanf.error.exception.RestApiException;
import com.capstone.wanf.profile.service.ProfileService;
import com.capstone.wanf.storage.service.S3Service;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.capstone.wanf.fixture.DomainFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class ClubPostServiceTest {
    @InjectMocks
    private ClubPostService clubPostService;

    @Mock
    private ProfileService profileService;

    @Mock
    private ClubService clubService;

    @Mock
    private ClubAuthService clubAuthService;

    @Mock
    private ClubAuthRepository clubAuthRepository;

    @Mock
    private S3Service s3Service;

    @Test
    void 모든_모임_게시글을_조회한다(){
        //given
        given(clubService.findById(any())).willReturn(모임2);
        //when
        List<ClubPostResponse> clubPosts = clubPostService.findAll(모임2.getId(), 유저1);
        //then
        assertThat(clubPosts.size()).isEqualTo(1);
    }

    @Test
    void 이미지_없는_모임_게시글을_저장한다(){
        //given
        given(clubService.findById(any())).willReturn(모임1);

        given(profileService.findByUser(any())).willReturn(프로필1);
        //when
        ClubPost clubPost = clubPostService.save(유저1, 모임1.getId(), 모임_게시글_요청1);
        //then
        assertThat(clubPost.getContent()).isEqualTo(모임_게시글_요청1.content());
    }

    @Test
    void 이미지_있는_모임_게시글을_저장한다(){
        //given
        given(clubService.findById(any())).willReturn(모임1);

        given(profileService.findByUser(any())).willReturn(프로필1);

        given(s3Service.findById(any())).willReturn(이미지1);
        //when
        ClubPost clubPost = clubPostService.save(유저1, 모임1.getId(), 모임_게시글_요청2);
        //then
        assertThat(clubPost.getContent()).isEqualTo(모임_게시글_요청2.content());

        assertThat(clubPost.getImage()).isEqualTo(이미지1);
    }

    @Test
    void 모임_게시글을_삭제한다(){
        //given
        given(clubService.findById(any())).willReturn(모임2);

        given(profileService.findByUser(any())).willReturn(프로필1);
        //when
        List<ClubPost> clubPosts  = clubPostService.delete(유저1, 모임2.getId(), 모임_게시글1.getId());
        //then
        assertThat(clubPosts.size()).isEqualTo(0);
    }

    @Test
    void 게시글_작성자가_아닌_사용자가_모임_게시글을_삭제할_수_없다(){
        //given
        given(clubService.findById(any())).willReturn(모임3);

        given(profileService.findByUser(any())).willReturn(프로필2);
        //when & then
        assertThatThrownBy(() -> clubPostService.delete(유저2, 모임3.getId(), 모임_게시글1.getId()))
                .isInstanceOf(RestApiException.class);
    }

    @Test
    void 이미지가_있는_게시물이며_이미지도_삭제한다(){
        //given
        given(clubService.findById(any())).willReturn(모임3);

        given(profileService.findByUser(any())).willReturn(프로필1);
        //when
        List<ClubPost> clubPosts  = clubPostService.delete(유저1, 모임3.getId(), 모임_게시글2.getId());
        //then
        assertThat(clubPosts.size()).isEqualTo(1);
    }

    @Test
    void ID에_해당하는_모임_게시글을_조회한다(){
        //given
        given(clubService.findById(any())).willReturn(모임3);
        //when
        ClubPost clubPost = clubPostService.findById(모임3.getId(), 모임_게시글1.getId());
        //then
        assertThat(clubPost).isEqualTo(모임_게시글1);
    }
}