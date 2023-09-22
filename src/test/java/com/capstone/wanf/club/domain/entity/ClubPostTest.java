package com.capstone.wanf.club.domain.entity;

import com.capstone.wanf.club.dto.response.ClubPostResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static com.capstone.wanf.fixture.DomainFixture.모임_게시글1;
import static com.capstone.wanf.fixture.DomainFixture.모임_게시글3;
import static org.junit.jupiter.api.Assertions.*;

class ClubPostTest {
    @Test
    void 모임_게시글을_DTO에_담는다(){
        //when
        ClubPostResponse clubPostResponse = 모임_게시글3.toResponse();
        //then
        assertAll(
                () -> Assertions.assertThat(clubPostResponse.content()).isEqualTo(모임_게시글1.getContent()),
                () -> Assertions.assertThat(clubPostResponse.image()).isNull()
        );
    }

}