package com.capstone.wanf.course.domain.repo;

import com.capstone.wanf.course.domain.entity.Major;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static com.capstone.wanf.fixture.DomainFixture.전공1;
import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MajorRepositoryTest {
    @Autowired
    private MajorRepository majorRepository;

    @Test
    void 전공ID로_전공을_조회한다(){
        //given
        final Major savedMajor = majorRepository.save(전공1);

        //when
        Major major = majorRepository.findById(savedMajor.getId()).orElseThrow();

        //then
        assertThat(major).isEqualTo(savedMajor);
    }
}