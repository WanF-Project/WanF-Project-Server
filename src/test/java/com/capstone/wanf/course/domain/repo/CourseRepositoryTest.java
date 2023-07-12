package com.capstone.wanf.course.domain.repo;

import com.capstone.wanf.config.TestQueryDslConfig;
import com.capstone.wanf.course.domain.entity.Course;
import com.capstone.wanf.course.domain.entity.Major;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static com.capstone.wanf.fixture.DomainFixture.전공1;
import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({TestQueryDslConfig.class})
class CourseRepositoryTest {
    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private MajorRepository majorRepository;

    @Autowired
    private JPAQueryFactory queryFactory;

    private CourseRepositorySupport courseRepositorySupport;

    private Major savedMajor;

    @BeforeEach
    void setUp() {
        savedMajor = majorRepository.save(전공1);

        courseRepositorySupport = new CourseRepositorySupport(queryFactory);
    }

    @Test
    void 강의ID로_강의를_조회한다(){
        //given
        final Course savedCourse = courseRepository.save(Course.builder()
                .courseId("과목코드")
                .name("강의명")
                .courseTime("강의시간")
                .professor("교수")
                .category("카테고리")
                .major(savedMajor)
                .courseId("강의코드")
                .build());

        //when
        Course course = courseRepository.findById(savedCourse.getId()).orElseThrow();

        //then
        assertThat(course).isEqualTo(savedCourse);
    }

    @Test
    void 강의_리스트의_길이가_0이면_빈_리스트를_반환한다(){
        //when
        List<Course> courses = courseRepository.findAll();
        //then
        assertThat(courses.size()).isEqualTo(0);
    }

    @Test
    void 강의명으로_해당하는_강의를_검색한다(){
        //given
        Course 수업1 = Course.builder()
                .courseId("과목코드")
                .name("수업1")
                .courseTime("수업시간")
                .professor("교수")
                .category("카테고리")
                .major(savedMajor)
                .courseId("과목코드")
                .build();

        Course 수업2 = Course.builder()
                .courseId("과목코드")
                .name("수업2")
                .courseTime("수업시간")
                .professor("교수")
                .category("카테고리")
                .major(savedMajor)
                .courseId("과목코드")
                .build();

        courseRepository.saveAll(List.of(수업1, 수업2));
        //when
        List<Course> searchedCourses = courseRepositorySupport.searchByQuery("수업1");
        //then
        assertThat(searchedCourses.size()).isEqualTo(1);
    }
}