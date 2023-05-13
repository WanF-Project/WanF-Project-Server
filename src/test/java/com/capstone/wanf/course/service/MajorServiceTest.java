package com.capstone.wanf.course.service;

import com.capstone.wanf.course.domain.entity.Major;
import com.capstone.wanf.course.domain.repo.MajorRepository;
import com.capstone.wanf.error.exception.RestApiException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class MajorServiceTest {
    @InjectMocks
    private MajorService majorService;

    @Mock
    private MajorRepository majorRepository;

    @Test
    void 전공_모두_조회시_전공이_없으면_빈_리스트를_반환한다() {
        //given
        given(majorRepository.findAll()).willReturn(List.of());
        //when
        final List<Major> majors = majorService.findAll();
        //then
        assertThat(majors).isEmpty();
    }

    @Test
    void ID로_전공_조회시_해당_전공이_없으면_예외를_던진다() {
        //given
        given(majorRepository.findById(anyLong())).willReturn(Optional.empty());
        //when & then
        assertThatThrownBy(() -> majorService.findById(1L))
                .isInstanceOf(RestApiException.class);
    }

    @Test
    void ID로_전공을_조회한다() {
        //given
        given(majorRepository.findById(anyLong())).willReturn(Optional.of(Major.builder()
                .name("과목1")
                .build()));
        //when
        Major major = majorService.findById(1L);
        //then
        assertThat(major.getName()).isEqualTo("과목1");
    }

}