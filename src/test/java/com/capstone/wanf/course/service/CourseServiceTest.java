package com.capstone.wanf.course.service;

import com.capstone.wanf.course.domain.entity.Course;
import com.capstone.wanf.course.domain.repo.CourseRepository;
import com.capstone.wanf.course.domain.repo.CourseRepositorySupport;
import com.capstone.wanf.course.dto.request.CourseRequest;
import com.capstone.wanf.error.exception.RestApiException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.capstone.wanf.fixture.DomainFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class CourseServiceTest {
    @InjectMocks
    private CourseService courseService;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private MajorService majorService;

    @Mock
    private CourseRepositorySupport courseRepositorySupport;

    @Test
    void ID로_강의_조회시_해당_강의가_없으면_예외를_던진다() {
        //given
        given(courseRepository.findById(anyLong())).willReturn(Optional.empty());
        //when & then
        assertThatThrownBy(() -> courseService.findById(1L))
                .isInstanceOf(RestApiException.class);
    }

    @Test
    void ID로_강의를_조회한다() {
        //given
        given(courseRepository.findById(anyLong())).willReturn(Optional.of(Course.builder()
                .name("강의1")
                .build()));
        //when
        Course course = courseService.findById(1L);
        //then
        assertThat(course.getName()).isEqualTo("강의1");
    }

    @Test
    void 강의_모두_조회시_강의가_없으면_빈_리스트를_반환한다() {
        //given
        given(courseRepository.findAll()).willReturn(List.of());
        //when
        final List<Course> courses = courseService.findAll();
        //then
        assertThat(courses).isEmpty();
    }

    @Nested
    class 강의_저장{
        @Test
        void 전공ID가_NULL_이어도_강의를_저장한다(){
            //given
            CourseRequest 강의_요청 = new CourseRequest("강의1", "전공필수", "월1,2,3", "김교수님", null);

            given(courseRepository.save(any(Course.class))).willReturn(Course.builder()
                    .name(강의_요청.getName())
                    .category(강의_요청.getCategory())
                    .courseTime(강의_요청.getCourseTime())
                    .professor(강의_요청.getProfessor())
                    .build());
            //when
            Course savedCourse = courseService.save(강의_요청);
            //then
            assertAll(() -> assertThat(savedCourse.getName()).isEqualTo(강의_요청.getName()),
                    () -> assertThat(savedCourse.getCategory()).isEqualTo(강의_요청.getCategory()),
                    () -> assertThat(savedCourse.getCourseTime()).isEqualTo(강의_요청.getCourseTime()),
                    () -> assertThat(savedCourse.getProfessor()).isEqualTo(강의_요청.getProfessor()),
                    () -> assertThat(savedCourse.getMajor()).isNull()
            );
        }

        @Test
        void 전공ID가_NULL이_아니라면_전공에_맞는_강의를_저장한다(){
            //given
            CourseRequest 강의_요청 = new CourseRequest("강의명", "카테고리", "강의시간", "강의코드", "교수", 1L);

            given(majorService.findById(anyLong())).willReturn(전공1);

            given(courseRepository.save(any(Course.class))).willReturn(Course.builder()
                    .name(강의_요청.getName())
                    .category(강의_요청.getCategory())
                    .courseTime(강의_요청.getCourseTime())
                    .professor(강의_요청.getProfessor())
                    .major(전공1)
                    .build());
            //when
            Course savedCourse = courseService.save(강의_요청);
            //then
            assertAll(
                    () -> assertThat(savedCourse.getName()).isEqualTo(강의_요청.getName()),
                    () -> assertThat(savedCourse.getCategory()).isEqualTo(강의_요청.getCategory()),
                    () -> assertThat(savedCourse.getCourseTime()).isEqualTo(강의_요청.getCourseTime()),
                    () -> assertThat(savedCourse.getProfessor()).isEqualTo(강의_요청.getProfessor()),
                    () -> assertThat(savedCourse.getMajor()).isEqualTo(전공1)
            );
        }
    }

    @Test
    void 검색어에_해당하는_강의를_모두_조회한다(){
        //given
        given(courseRepositorySupport.searchByQuery(anyString())).willReturn(List.of(강의1, 강의2));
        //when
        List<Course> courses = courseService.searchByQuery("검색어");
        //then
        assertThat(courses).hasSize(2);
    }
}