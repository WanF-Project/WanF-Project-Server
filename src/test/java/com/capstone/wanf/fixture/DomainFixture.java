package com.capstone.wanf.fixture;

import com.capstone.wanf.comment.domain.entity.Comment;
import com.capstone.wanf.comment.dto.request.CommentRequest;
import com.capstone.wanf.course.domain.entity.Course;
import com.capstone.wanf.course.domain.entity.Major;
import com.capstone.wanf.course.dto.request.CourseRequest;
import com.capstone.wanf.post.domain.entity.Category;
import com.capstone.wanf.post.domain.entity.Post;
import com.capstone.wanf.post.dto.request.PostRequest;
import com.capstone.wanf.post.dto.response.PostPaginationResponse;
import com.capstone.wanf.profile.domain.entity.*;
import com.capstone.wanf.profile.dto.request.ProfileRequest;
import com.capstone.wanf.user.domain.entity.Role;
import com.capstone.wanf.user.domain.entity.User;
import com.capstone.wanf.user.dto.request.CodeRequest;
import com.capstone.wanf.user.dto.request.EmailRequest;
import com.capstone.wanf.user.dto.request.UserRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DomainFixture {
    public static final Major 전공1 = Major.builder()
            .name("전공1")
            .build();

    public static final Major 전공2 = Major.builder()
            .name("전공2")
            .build();

    public static final Course 강의1 = Course.builder()
            .id(1L)
            .name("강의1")
            .category("카테고리1")
            .courseTime("강의시간1")
            .courseId("강의코드1")
            .professor("교수1")
            .build();

    public static final Course 강의2 = Course.builder()
            .id(2L)
            .name("강의2")
            .category("카테고리2")
            .courseTime("강의시간2")
            .courseId("강의코드2")
            .professor("교수2")
            .build();

    public static final User 유저1 = User.builder()
            .email("royqwe16@gmail.com")
            .userPassword("1234@@qwer")
            .verificationCode("1234")
            .role(Role.USER)
            .build();

    public static final User 유저2 = User.builder()
            .email("wanf@gmail.com")
            .userPassword("1234@@qwer")
            .verificationCode("5678")
            .role(Role.USER)
            .build();

    public static final User 유저3 = User.builder()
            .email("wanf@gmail.com")
            .userPassword(null)
            .verificationCode("5678")
            .role(Role.USER)
            .build();

    public static final Profile 프로필1 = Profile.builder()
            .nickname("닉네임1")
            .studentId(12345678)
            .age(1)
            .user(유저1)
            .contact("연락처1")
            .profileImage(ProfileImage.BEAR)
            .gender(Gender.MALE)
            .mbti(MBTI.INFJ)
            .personalities(List.of(Personality.BRAVERY))
            .goals(List.of(Goal.GRADUATE))
            .build();

    public static final Profile 프로필2 = Profile.builder()
            .nickname("닉네임2")
            .studentId(12345678)
            .age(1)
            .contact("연락처2")
            .profileImage(ProfileImage.BEAR)
            .gender(Gender.MALE)
            .mbti(MBTI.INFJ)
            .personalities(List.of(Personality.BRAVERY))
            .goals(List.of(Goal.GRADUATE))
            .user(유저1)
            .major(전공1)
            .build();

    public static final Profile 프로필3 = Profile.builder()
            .nickname("닉네임3")
            .studentId(12345678)
            .age(1)
            .contact("연락처3")
            .profileImage(ProfileImage.BEAR)
            .gender(Gender.MALE)
            .mbti(MBTI.INFJ)
            .personalities(List.of(Personality.BRAVERY))
            .goals(List.of(Goal.GRADUATE))
            .user(유저1)
            .major(전공2)
            .build();

    public static final ProfileRequest 프로필_수정1 = ProfileRequest.builder()
            .majorId(1L)
            .age(0)
            .studentId(0)
            .build();

    public static final ProfileRequest 프로필_수정2 = ProfileRequest.builder()
            .goals(List.of(Goal.GRADUATE))
            .gender(Gender.MALE)
            .contact("연락처2")
            .profileImage(ProfileImage.BEAR)
            .studentId(12345678)
            .age(1)
            .mbti(MBTI.INFJ)
            .personalities(List.of(Personality.BRAVERY))
            .nickname("닉네임2")
            .build();

    public static final ProfileRequest 프로필_수정3 = ProfileRequest.builder()
            .goals(List.of(Goal.GRADUATE))
            .gender(Gender.MALE)
            .contact("연락처3")
            .profileImage(ProfileImage.BEAR)
            .studentId(12345678)
            .age(1)
            .mbti(MBTI.INFJ)
            .personalities(List.of(Personality.BRAVERY))
            .nickname("닉네임3")
            .majorId(1L)
            .build();

    public static final CourseRequest 강의_요청1 = new CourseRequest("강의명", "카테고리", "강의시간", "과목코드", "교수");

    public static final Post 게시글1 = Post.builder()
            .id(1L)
            .title("게시글1")
            .content("게시글1")
            .course(강의1)
            .category(Category.friend)
            .profile(프로필1)
            .comments(new ArrayList<>())
            .build();

    public static final Post 게시글2 = Post.builder()
            .id(2L)
            .title("게시글2")
            .content("게시글2")
            .category(Category.friend)
            .profile(프로필1)
            .build();

    public static final PostRequest 게시글_요청1 = PostRequest.builder()
            .title("게시글_요청1")
            .content("게시글_요청1")
            .courseId(1L)
            .build();

    public static final PostRequest 게시글_요청2 = PostRequest.builder()
            .title("게시글_요청2")
            .content("게시글_요청2")
            .courseId(1L)
            .build();

    public static final Comment 댓글1 = Comment.builder()
            .id(1L)
            .content("댓글1")
            .profile(프로필1)
            .build();

    public static final CommentRequest 댓글_요청1 = CommentRequest.builder()
            .content("댓글_요청1")
            .build();

    public static final CommentRequest 댓글_요청2 = CommentRequest.builder()
            .content("댓글_요청2")
            .build();

    public static final CommentRequest 댓글_요청3 = CommentRequest.builder()
            .content("댓글_요청3")
            .build();

    public static final Post 게시글3 = Post.builder()
            .id(1L)
            .title("게시글1")
            .content("게시글1")
            .course(강의1)
            .category(Category.friend)
            .profile(프로필1)
            .comments(new ArrayList<>(Arrays.asList(댓글1)))
            .build();

    public static final Post 게시글4 = Post.builder()
            .id(1L)
            .title("게시글1")
            .content("게시글1")
            .course(강의1)
            .category(Category.friend)
            .profile(프로필1)
            .comments(new ArrayList<>(Arrays.asList(댓글1)))
            .build();

    public static final PostPaginationResponse 게시글_페이징_응답1 = PostPaginationResponse.builder()
            .course(강의1.toCoursePaginationResponse())
            .title(게시글1.getTitle())
            .id(게시글1.getId())
            .createdDate(게시글1.getCreatedDate())
            .modifiedDate(게시글1.getModifiedDate())
            .build();

    public static final PostPaginationResponse 게시글_페이징_응답2 = PostPaginationResponse.builder()
            .course(강의1.toCoursePaginationResponse())
            .title(게시글2.getTitle())
            .id(게시글2.getId())
            .createdDate(게시글2.getCreatedDate())
            .modifiedDate(게시글2.getModifiedDate())
            .build();

    public static final UserRequest 회원가입_요청1 = UserRequest.builder()
            .email("test@email.com")
            .userPassword("0000")
            .build();

    public static final CodeRequest 인증번호_요청1 = CodeRequest.builder()
            .email("test@email.com")
            .verificationCode("1234")
            .build();

    public static final EmailRequest 이메일_요청1 = EmailRequest.builder()
            .email("mongbu54@gmail.com")
            .build();
}