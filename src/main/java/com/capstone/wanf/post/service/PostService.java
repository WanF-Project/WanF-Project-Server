package com.capstone.wanf.post.service;

import com.capstone.wanf.course.domain.entity.Course;
import com.capstone.wanf.course.service.CourseService;
import com.capstone.wanf.error.exception.RestApiException;
import com.capstone.wanf.post.domain.entity.Category;
import com.capstone.wanf.post.domain.entity.Post;
import com.capstone.wanf.post.domain.repo.PostRepository;
import com.capstone.wanf.post.domain.repo.PostRepositorySupport;
import com.capstone.wanf.post.dto.request.RequestPost;
import com.capstone.wanf.profile.domain.entity.Profile;
import com.capstone.wanf.profile.service.ProfileService;
import com.capstone.wanf.user.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.capstone.wanf.error.errorcode.CustomErrorCode.POST_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    private final PostRepositorySupport postRepositorySupport;

    private final ProfileService profileService;

    private final CourseService courseService;

    @Transactional(readOnly = true)
    public Slice<Post> findAll(Category category, Pageable pageable) {
        return postRepositorySupport.findAll(category, pageable);
    }

    @Transactional
    public Post save(Category category, RequestPost requestPost, User user) {

        Profile profile = profileService.findByUser(user);

        Course course = courseService.findById(requestPost.getCourseId());

        Post post = Post.builder()
                .category(category)
                .profile(profile)
                .course(course)
                .title(requestPost.getTitle())
                .content(requestPost.getContent())
                .build();

        return postRepository.save(post);
    }

    @Transactional(readOnly = true)
    public Post findById(Long id) {
        return postRepository.findById(id).orElseThrow(() -> new RestApiException(POST_NOT_FOUND));
    }

    @Transactional
    public Post update(Long id, RequestPost requestPost) {
        Post post = postRepository.findById(id).orElseThrow(() -> new RestApiException(POST_NOT_FOUND));

        post.update(requestPost);

        return postRepository.save(post);
    }

    @Transactional
    public void delete(Long id) {
        postRepository.deleteById(id);
    }
}
