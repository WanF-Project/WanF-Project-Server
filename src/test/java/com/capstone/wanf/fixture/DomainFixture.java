package com.capstone.wanf.fixture;

import com.capstone.wanf.club.domain.entity.Authority;
import com.capstone.wanf.club.domain.entity.Club;
import com.capstone.wanf.club.domain.entity.ClubAuth;
import com.capstone.wanf.club.domain.entity.ClubPost;
import com.capstone.wanf.club.dto.request.ClubPostRequest;
import com.capstone.wanf.club.dto.request.ClubPwdRequest;
import com.capstone.wanf.club.dto.request.ClubRequest;
import com.capstone.wanf.comment.domain.entity.Comment;
import com.capstone.wanf.comment.dto.request.CommentRequest;
import com.capstone.wanf.course.domain.entity.Course;
import com.capstone.wanf.course.domain.entity.Major;
import com.capstone.wanf.course.dto.request.CourseRequest;
import com.capstone.wanf.message.domain.entity.KafkaMessage;
import com.capstone.wanf.message.domain.entity.Message;
import com.capstone.wanf.message.dto.request.MessageRequest;
import com.capstone.wanf.message.dto.response.MessageResponse;
import com.capstone.wanf.post.domain.entity.Category;
import com.capstone.wanf.post.domain.entity.Post;
import com.capstone.wanf.post.dto.request.PostRequest;
import com.capstone.wanf.post.dto.response.PostPaginationResponse;
import com.capstone.wanf.profile.domain.entity.*;
import com.capstone.wanf.profile.dto.request.ProfileImageRequest;
import com.capstone.wanf.profile.dto.request.ProfileRequest;
import com.capstone.wanf.storage.domain.entity.Directory;
import com.capstone.wanf.storage.domain.entity.Image;
import com.capstone.wanf.user.domain.entity.Role;
import com.capstone.wanf.user.domain.entity.User;
import com.capstone.wanf.user.dto.request.CodeRequest;
import com.capstone.wanf.user.dto.request.EmailRequest;
import com.capstone.wanf.user.dto.request.UserRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DomainFixture {
    public static final Major 전공1 = Major.builder()
            .id(1L)
            .name("전공1")
            .build();

    public static final Major 전공2 = Major.builder()
            .id(2L)
            .name("전공2")
            .build();

    public static final Image 이미지1 = Image.builder()
            .id(1L)
            .imageUrl("https://test.com")
            .directory(Directory.profiles)
            .convertImageName("test")
            .build();

    public static final Image 이미지2 = Image.builder()
            .id(2L)
            .imageUrl("https://second.com")
            .directory(Directory.profiles)
            .convertImageName("second")
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
            .id(1L)
            .email("royqwe16@gmail.com")
            .userPassword("1234@@qwer")
            .verificationCode("1234")
            .fcmTokens(new ArrayList<>(List.of("fcmToken1", "fcmToken2")))
            .role(Role.USER)
            .build();

    public static final User 유저2 = User.builder()
            .id(2L)
            .email("wanf@gmail.com")
            .userPassword("1234@@qwer")
            .verificationCode("5678")
            .fcmTokens(new ArrayList<>(List.of("fcmToken1", "fcmToken2", "fcmToken3")))
            .role(Role.USER)
            .build();

    public static final User 유저3 = User.builder()
            .email("wanf@gmail.com")
            .userPassword(null)
            .verificationCode("5678")
            .role(Role.USER)
            .build();

    public static final Profile 프로필1 = Profile.builder()
            .id(1L)
            .nickname("닉네임1")
            .studentId(201814100)
            .age(24)
            .user(유저1)
            .image(이미지1)
            .gender(Gender.MALE)
            .mbti(MBTI.INFJ)
            .personalities(List.of(Personality.BRAVERY))
            .goals(List.of(Goal.GRADUATE))
            .build();

    public static final Profile 프로필2 = Profile.builder()
            .id(2L)
            .nickname("닉네임2")
            .studentId(201814100)
            .age(21)
            .image(이미지1)
            .gender(Gender.MALE)
            .mbti(MBTI.INFJ)
            .personalities(List.of(Personality.BRAVERY))
            .goals(List.of(Goal.GRADUATE))
            .user(유저1)
            .major(전공1)
            .build();

    public static final Profile 프로필3 = Profile.builder()
            .nickname("닉네임3")
            .studentId(201814000)
            .age(21)
            .image(이미지2)
            .gender(Gender.MALE)
            .mbti(MBTI.INFJ)
            .personalities(List.of(Personality.BRAVERY))
            .goals(List.of(Goal.GRADUATE))
            .user(유저1)
            .major(전공2)
            .build();

    public static final ProfileRequest 프로필_수정1 = ProfileRequest.builder()
            .goals(List.of(Goal.GRADUATE))
            .gender(Gender.MALE)
            .majorId(2L)
            .studentId(201814000)
            .age(21)
            .mbti(MBTI.INFJ)
            .personalities(List.of(Personality.BRAVERY))
            .nickname("닉네임2")
            .build();

    public static final ProfileRequest 프로필_수정2 = ProfileRequest.builder()
            .goals(List.of(Goal.GRADUATE))
            .gender(Gender.MALE)
            .studentId(201814000)
            .age(21)
            .mbti(MBTI.INFJ)
            .personalities(List.of(Personality.BRIGHT))
            .nickname("닉네임3")
            .majorId(1L)
            .build();

    public static final ProfileRequest 프로필_수정3 = ProfileRequest.builder()
            .goals(List.of(Goal.GRADUATE))
            .gender(Gender.MALE)
            .studentId(201814000)
            .age(21)
            .mbti(MBTI.INFJ)
            .personalities(List.of(Personality.BRAVERY))
            .nickname("닉네임3")
            .majorId(1L)
            .build();

    public static final ProfileImageRequest 프로필_이미지_수정1 = ProfileImageRequest.builder()
            .imageId(1L)
            .profileRequest(프로필_수정1)
            .build();

    public static final ProfileImageRequest 프로필_이미지_수정2 = ProfileImageRequest.builder()
            .imageId(1L)
            .profileRequest(프로필_수정2)
            .build();

    public static final ProfileImageRequest 프로필_이미지_수정3 = ProfileImageRequest.builder()
            .imageId(1L)
            .profileRequest(프로필_수정3)
            .build();

    public static final ProfileRequest 프로필_저장 = ProfileRequest.builder()
            .goals(List.of(Goal.GRADUATE))
            .gender(Gender.MALE)
            .studentId(201814000)
            .age(21)
            .mbti(MBTI.INFJ)
            .personalities(List.of(Personality.BRIGHT))
            .nickname("닉네임3")
            .majorId(1L)
            .build();

    public static final ProfileImageRequest 프로필_이미지_저장 = ProfileImageRequest.builder()
            .imageId(1L)
            .profileRequest(프로필_저장)
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

    public static final Comment 댓글2 = Comment.builder()
            .id(2L)
            .content("댓글2")
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
            .comments(new ArrayList<>(Collections.singletonList(댓글1)))
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

    public static final MessageRequest 쪽지_요청1 = MessageRequest.builder()
            .receiverProfileId(1L)
            .content("쪽지_요청1")
            .build();

    public static final MessageRequest 쪽지_요청2 = MessageRequest.builder()
            .receiverProfileId(2L)
            .content("쪽지_요청2")
            .build();
    public static final KafkaMessage 카프카_쪽지1 = KafkaMessage.builder()
            .receiverProfileId(1L)
            .sender(유저1)
            .content("카프카_쪽지1")
            .build();

    public static final Message 쪽지1 = Message.builder()
            .receiverProfile(프로필1)
            .senderProfile(프로필2)
            .content("쪽지1")
            .build();

    public static final MessageResponse 쪽지_응답1 = MessageResponse.builder()
            .senderProfileId(1L)
            .content("쪽지_응답1")
            .build();

    public static final MessageResponse 쪽지_응답2 = MessageResponse.builder()
            .senderProfileId(1L)
            .content("쪽지_응답2")
            .build();

    public static final List<MessageResponse> 쪽지_목록1 = List.of(쪽지_응답1, 쪽지_응답2);

    public static final ClubPost 모임_게시글1 = ClubPost.builder()
            .id(1L)
            .image(이미지1)
            .content("모임_게시글1")
            .profile(프로필1)
            .build();

    public static final ClubPost 모임_게시글2 = ClubPost.builder()
            .id(2L)
            .content("모임_게시글1")
            .profile(프로필1)
            .image(이미지2)
            .build();

    public static final ClubPost 모임_게시글3 = ClubPost.builder()
            .id(3L)
            .content("모임_게시글1")
            .profile(프로필1)
            .build();

    public static final Club 모임1 = Club.builder()
            .id(1L)
            .name("클럽1")
            .password("1234")
            .maxParticipants(5)
            .currentParticipants(4)
            .recruitmentStatus(false)
            .build();

    public static final Club 모임2 = Club.builder()
            .id(1L)
            .name("클럽1")
            .password("0000")
            .maxParticipants(10)
            .currentParticipants(1)
            .recruitmentStatus(false)
            .posts(new ArrayList<>(List.of(모임_게시글1)))
            .build();

    public static final Club 모임3 = Club.builder()
            .id(1L)
            .name("클럽1")
            .maxParticipants(3)
            .currentParticipants(3)
            .recruitmentStatus(true)
            .posts(new ArrayList<>(List.of(모임_게시글1, 모임_게시글2)))
            .build();

    public static final ClubAuth 리더모임권한1 = ClubAuth.builder()
            .user(유저1)
            .club(모임1)
            .authority(Authority.CLUB_LEADER)
            .build();

    public static final ClubAuth 모임권한없음1 = ClubAuth.builder()
            .user(유저2)
            .club(모임1)
            .authority(Authority.NONE)
            .build();

    public static final ClubPostRequest 모임_게시글_요청1 = ClubPostRequest
            .builder()
            .content("모임_게시글_요청1")
            .build();

    public static final ClubPostRequest 모임_게시글_요청2 = ClubPostRequest
            .builder()
            .content("모임_게시글_요청2")
            .imageId(1L)
            .build();

    public static final ClubPwdRequest 모임_비밀번호_요청1 = ClubPwdRequest
            .builder()
            .clubId(1L)
            .password("1234")
            .build();

    public static final ClubRequest 모임_요청1 = ClubRequest
            .builder()
            .name("모임_요청1")
            .maxParticipants(1)
            .password("1234")
            .build();

    public static final ClubRequest 모임_요청2 = ClubRequest
            .builder()
            .name("모임_요청2")
            .maxParticipants(5)
            .password("1234")
            .build();
}