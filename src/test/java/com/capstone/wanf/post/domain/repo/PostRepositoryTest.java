package com.capstone.wanf.post.domain.repo;

import com.capstone.wanf.config.TestQueryDslConfig;
import com.capstone.wanf.course.domain.entity.Course;
import com.capstone.wanf.course.domain.repo.CourseRepository;
import com.capstone.wanf.fixture.DomainFixture;
import com.capstone.wanf.post.domain.entity.Category;
import com.capstone.wanf.post.domain.entity.Post;
import com.capstone.wanf.post.dto.response.PostPaginationResponse;
import com.capstone.wanf.profile.domain.entity.*;
import com.capstone.wanf.profile.domain.repo.ProfileRepository;
import com.capstone.wanf.user.domain.entity.User;
import com.capstone.wanf.user.domain.repo.UserRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.*;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(TestQueryDslConfig.class)
class PostRepositoryTest {
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private ProfileRepository profileRepository;


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JPAQueryFactory queryFactory;

    private PostRepositorySupport postRepositorySupport;

    private Course 수업1;

    private Profile 프로필1;

    private User 유저1;

    @BeforeEach
    void setUp(){
        postRepositorySupport = new PostRepositorySupport(queryFactory);

        유저1 = userRepository.save(DomainFixture.유저1);

        수업1 = courseRepository.save(DomainFixture.수업1);

        프로필1 = profileRepository.save(Profile.builder()
                .nickname("닉네임1")
                .studentId(12345678)
                .age(1)
                .contact("연락처1")
                .profileImage(ProfileImage.BEAR)
                .gender(Gender.MALE)
                .mbti(MBTI.INFJ)
                .personalities(List.of(Personality.BRAVERY))
                .goals(List.of(Goal.GRADUATE))
                .user(유저1)
                .build());
    }
    @Test
    void 게시글_ID로_게시글을_조회할_수_있다() {
        //given
        Post post = Post.builder()
                .title("title")
                .content("content")
                .category(Category.friend)
                .course(수업1)
                .profile(프로필1)
                .build();

        Post savedPost = postRepository.save(post);
        //when
        Post findPost = postRepository.findById(savedPost.getId()).orElseThrow();
        //then
        assertThat(findPost).isEqualTo(savedPost);
    }

    @Nested
    class 카테고리별_게시물을_모두_조회한다 {
        @Test
        void 페이징을_적용하지_않는_모두조회() {
            //given
            Post post1 = Post.builder()
                    .title("title1")
                    .content("content")
                    .category(Category.friend)
                    .course(수업1)
                    .profile(프로필1)
                    .build();

            Post post2 = Post.builder()
                    .title("title2")
                    .content("content")
                    .category(Category.friend)
                    .course(수업1)
                    .profile(프로필1)
                    .build();

            postRepository.saveAll(List.of(post1, post2));
            //when
            List<PostPaginationResponse> postList = postRepositorySupport.findAll(Category.friend);
            //then
            assertThat(postList).hasSize(2);
        }

        @Test
        void 디폴트_정렬_페이징() {
            //given
            Post post1 = Post.builder()
                    .title("title1")
                    .content("content")
                    .category(Category.friend)
                    .course(수업1)
                    .profile(프로필1)
                    .build();

            Post post2 = Post.builder()
                    .title("title2")
                    .content("content")
                    .category(Category.friend)
                    .course(수업1)
                    .profile(프로필1)
                    .build();

            postRepository.saveAll(List.of(post1, post2));

            Pageable pageable = PageRequest.of(0, 5);

            //when
            Slice<PostPaginationResponse> posts = postRepositorySupport.findAll(Category.friend, pageable);
            //then
            assertThat(posts).hasSize(2);
        }

        @Test
        void 수정날짜로_정렬된_페이징() {
            //given
            Post post1 = Post.builder()
                    .title("title1")
                    .content("content")
                    .category(Category.friend)
                    .course(수업1)
                    .profile(프로필1)
                    .build();

            Post post2 = Post.builder()
                    .title("title2")
                    .content("content")
                    .category(Category.friend)
                    .course(수업1)
                    .profile(프로필1)
                    .build();

            postRepository.saveAll(List.of(post1, post2));

            Sort.Order order = new Sort.Order(Sort.Direction.DESC, "modifiedDate");

            Sort sort = Sort.by(order);

            Pageable pageable = PageRequest.of(0, 5, sort);

            //when
            Slice<PostPaginationResponse> posts = postRepositorySupport.findAll(Category.friend, pageable);
            //then
            assertThat(posts).hasSize(2);
        }

        @Test
        void ID로_정렬된_페이징() {
            //given
            Post post1 = Post.builder()
                    .title("title1")
                    .content("content")
                    .category(Category.friend)
                    .course(수업1)
                    .profile(프로필1)
                    .build();

            Post post2 = Post.builder()
                    .title("title2")
                    .content("content")
                    .category(Category.friend)
                    .course(수업1)
                    .profile(프로필1)
                    .build();

            postRepository.saveAll(List.of(post1, post2));

            Sort.Order order = new Sort.Order(Sort.Direction.DESC, "id");

            Sort sort = Sort.by(order);

            Pageable pageable = PageRequest.of(0, 5, sort);

            //when
            Slice<PostPaginationResponse> posts = postRepositorySupport.findAll(Category.friend, pageable);
            //then
            assertAll(
                    () -> assertThat(posts).hasSize(2),
                    () -> assertThat(posts.getContent().get(0).id()).isGreaterThan(posts.getContent().get(1).id())
            );
        }

        @Test
        void DESC순으로_정렬된_페이징() {
            //given
            Post post1 = Post.builder()
                    .title("title1")
                    .content("content")
                    .category(Category.friend)
                    .course(수업1)
                    .profile(프로필1)
                    .build();

            Post post2 = Post.builder()
                    .title("title2")
                    .content("content")
                    .category(Category.friend)
                    .course(수업1)
                    .profile(프로필1)
                    .build();

            postRepository.saveAll(List.of(post1, post2));

            Sort.Order order = new Sort.Order(Sort.Direction.DESC, "ex");
            Sort sort = Sort.by(order);

            Pageable pageable = PageRequest.of(0, 5, sort);

            //when
            Slice<PostPaginationResponse> posts = postRepositorySupport.findAll(Category.friend, pageable);
            //then
            assertThat(posts).hasSize(2);
        }

        @Test
        void ASC순으로_정렬된_페이징() {
            //given
            Post post1 = Post.builder()
                    .title("title1")
                    .content("content")
                    .category(Category.friend)
                    .course(수업1)
                    .profile(프로필1)
                    .build();

            Post post2 = Post.builder()
                    .title("title2")
                    .content("content")
                    .category(Category.friend)
                    .course(수업1)
                    .profile(프로필1)
                    .build();

            postRepository.saveAll(List.of(post1, post2));

            Sort.Order order = new Sort.Order(Sort.Direction.ASC, "ex");

            Sort sort = Sort.by(order);

            Pageable pageable = PageRequest.of(0, 5, sort);

            //when
            Slice<PostPaginationResponse> posts = postRepositorySupport.findAll(Category.friend, pageable);
            //then
            assertThat(posts).hasSize(2);
        }

        @Test
        void 게시물이_없는_경우_비어있는_리스트를_반환한다() {
            //when
            List<PostPaginationResponse> posts = postRepositorySupport.findAll(Category.friend);
            //then
            assertThat(posts).isEmpty();
        }

        @Test
        void 조회된_데이터가_해당_페이지_이후에도_있으면_hasNext_속성이_TRUE가_된다(){
            //given
            Post post1 = Post.builder()
                    .title("title1")
                    .content("content")
                    .category(Category.friend)
                    .course(수업1)
                    .profile(프로필1)
                    .build();

            Post post2 = Post.builder()
                    .title("title2")
                    .content("content")
                    .category(Category.friend)
                    .course(수업1)
                    .profile(프로필1)
                    .build();

            postRepository.saveAll(List.of(post1, post2));

            Pageable pageable = PageRequest.of(0, 1);
            //when
            Slice<PostPaginationResponse> posts = postRepositorySupport.findAll(Category.friend, pageable);
            //then
            assertThat(posts.hasNext()).isTrue();
        }
    }

    @Test
    void 검색어에_해당하는_게시글을_조회한다(){
        //given
        Post post1 = Post.builder()
                .title("title1")
                .content("content")
                .category(Category.friend)
                .course(수업1)
                .profile(프로필1)
                .build();

        Post post2 = Post.builder()
                .title("title2")
                .content("content")
                .category(Category.friend)
                .course(수업1)
                .profile(프로필1)
                .build();

        postRepository.saveAll(List.of(post1, post2));
        //when
        Slice<PostPaginationResponse> posts = postRepositorySupport.searchAllByQuery(Category.friend, "title1", PageRequest.of(0, 5));
        //then
        assertThat(posts).hasSize(1);
    }
}