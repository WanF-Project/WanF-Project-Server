package com.capstone.wanf.course.domain.repo;

import com.capstone.wanf.course.domain.entity.Course;
import com.capstone.wanf.major.domain.entity.Major;
import com.capstone.wanf.major.domain.entity.repo.MajorRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static com.capstone.wanf.fixture.DomainFixture.전공1;
import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CourseRepositoryTest {
    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private MajorRepository majorRepository;

    @Test
    void 수업ID로_수업을_조회한다(){
        //given
        final Major savedMajor = majorRepository.save(전공1);

        final Course savedCourse = courseRepository.save(Course.builder()
                .courseId("과목코드")
                .name("수업명")
                .courseTime("수업시간")
                .professor("교수")
                .category("카테고리")
                .major(savedMajor)
                .courseId("과목코드")
                .build());

        //when
        Course course = courseRepository.findById(savedCourse.getId()).orElseThrow();

        //then
        assertThat(course).isEqualTo(savedCourse);
    }

    @Test
    void 수업_리스트의_길이가_0이면_빈_리스트를_반환한다(){
        //when
        List<Course> courses = courseRepository.findAll();
        //then
        assertThat(courses).isEmpty();
    }

}