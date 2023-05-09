package com.capstone.wanf.course.service;

import com.capstone.wanf.course.domain.entity.Course;
import com.capstone.wanf.course.domain.repo.CourseRepository;
import com.capstone.wanf.error.exception.RestApiException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class CourseServiceTest {
    @InjectMocks
    private CourseService courseService;

    @Mock
    private CourseRepository courseRepository;

    @Test
    void ID로_수업_조회시_해당_수업이_없으면_예외를_던진다() {
        //given
        given(courseRepository.findById(anyLong())).willReturn(Optional.empty());
        //when & then
        assertThatThrownBy(() -> courseService.findById(1L))
                .isInstanceOf(RestApiException.class);
    }

    @Test
    void 수업_모두_조회시_수업이_없으면_빈_리스트를_반환한다() {
        //given
        given(courseRepository.findAll()).willReturn(List.of());
        //when
        final List<Course> courses = courseService.findAll();
        //then
        assertThat(courses).isEmpty();
    }
}