package com.capstone.wanf.post.service;

import com.capstone.wanf.course.service.CourseService;
import com.capstone.wanf.error.exception.RestApiException;
import com.capstone.wanf.post.domain.entity.Category;
import com.capstone.wanf.post.domain.entity.Post;
import com.capstone.wanf.post.domain.repo.PostRepository;
import com.capstone.wanf.post.domain.repo.PostRepositorySupport;
import com.capstone.wanf.profile.service.ProfileService;
import com.capstone.wanf.user.domain.entity.User;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.util.List;
import java.util.Optional;

import static com.capstone.wanf.fixture.DomainFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {
    @InjectMocks
    private PostService postService;

    @Mock
    private PostRepository postRepository;

    @Mock
    private PostRepositorySupport postRepositorySupport;

    @Mock
    private ProfileService profileService;

    @Mock
    private CourseService courseService;

    @Test
    void 페이징_없이_게시글을_모두_조회한다(){
        //given
        given(postRepositorySupport.findAll(Category.friend)).willReturn(List.of(게시글1, 게시글2));
        //when
        List<Post> posts = postService.findAll(Category.friend);
        //then
        assertThat(posts).hasSize(2);
    }

    @Test
    void 페이징_있는_게시글을_모두_조회한다(){
        //given
        Pageable pageable = PageRequest.of(0, 5);

        given(postRepositorySupport.findAll(Category.friend,pageable)).willReturn(new SliceImpl<>(List.of(게시글1, 게시글2)));
        //when
        Slice<Post> posts = postService.findAll(Category.friend, pageable);
        //then
        assertThat(posts).hasSize(2);
    }

    @Test
    void ID로_게시글을_조회한다(){
        //given
        given(postRepository.findById(anyLong())).willReturn(Optional.of(게시글1));
        //when
        Post post = postService.findById(1L);
        //then
        assertThat(post).isEqualTo(게시글1);
    }

    @Test
    void ID로_게시글_조회시_없는_게시물일_경우_예외가_발생한다(){
        //given
        given(postRepository.findById(anyLong())).willReturn(Optional.empty());
        //when & then
        assertThatThrownBy(() -> postService.findById(1L))
                .isInstanceOf(RestApiException.class);
    }

    @Test
    void 게시글을_생성한다(){
        //given
        given(profileService.findByUser(any(User.class))).willReturn(프로필1);

        given(courseService.findById(anyLong())).willReturn(수업1);

        given(postRepository.save(any(Post.class))).willReturn(게시글1);
        //when
        Post post = postService.save(Category.friend, 게시글_요청1, 유저1);
        //then
        assertThat(post).isEqualTo(게시글1);
    }

    @Nested
    class 게시글을_수정한다{
        @Test
        void 내용만_수정한다(){
            //given
            given(postRepository.findById(anyLong())).willReturn(Optional.of(게시글1));

            given(courseService.findById(anyLong())).willReturn(게시글1.getCourse());
            //when
            Post post = postService.update(1L, 게시글_요청1);
            //then
            assertThat(post.getContent()).isEqualTo(게시글_요청1.content());
        }

        @Test
        void 내용과_수업을_수정한다(){
            //given
            given(postRepository.findById(anyLong())).willReturn(Optional.of(게시글1));

            given(courseService.findById(anyLong())).willReturn(수업2);
            //when
            Post post = postService.update(1L, 게시글_요청1);
            //then
            assertAll(
                    () -> assertThat(post.getTitle()).isEqualTo(게시글_요청1.title()),
                    () -> assertThat(post.getContent()).isEqualTo(게시글_요청1.content()),
                    () -> assertThat(post.getCourse()).isEqualTo(수업2)
            );
        }

        @Test
        void 게시물_수정시_없는_게시물일_경우_예외가_발생한다(){
            //given
            given(postRepository.findById(anyLong())).willReturn(Optional.empty());
            //when & then
            assertThatThrownBy(() -> postService.update(1L, 게시글_요청1))
                    .isInstanceOf(RestApiException.class);
        }
    }
    @Test
    void 게시글을_삭제한다() {
        //given
        willDoNothing().given(postRepository).deleteById(anyLong());
        //when
        postService.delete(게시글1.getId());
        //then
        then(postRepository).should(times(1)).deleteById(anyLong());
    }
}