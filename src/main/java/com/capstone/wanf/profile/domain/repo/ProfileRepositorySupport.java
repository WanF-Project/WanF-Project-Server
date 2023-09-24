package com.capstone.wanf.profile.domain.repo;

import com.capstone.wanf.profile.domain.entity.Profile;
import com.capstone.wanf.profile.domain.entity.QProfile;
import com.capstone.wanf.profile.dto.response.ProfileResponse;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ProfileRepositorySupport {
    private final JPAQueryFactory jpaQueryFactory;

    private final QProfile profile = QProfile.profile;

    /**
     * 랜덤으로 프로필 조회
     * querydsl은 random 함수를 지원하지 않음. 따라서 native query를 사용해야 함
     * MySQL은 NumberExpression을 지원 X. 따라서 Expressions.numberTemplate을 사용
     * @param pageable 페이지 정보
     * @param myProfileId 내 프로필 id
     * @return 프로필 리스트
     */
    public Slice<ProfileResponse> findByRandom(Pageable pageable, Long myProfileId) {
        List<Profile> profileList = jpaQueryFactory
                .selectFrom(profile)
                .where(profile.id.ne(myProfileId))
                .orderBy(Expressions.numberTemplate(Double.class, "RAND()").asc())
                .limit(pageable.getPageSize() + 1)
                .offset(pageable.getOffset())
                .fetch();

        return ToSliceProfileImpl(pageable, profileList);
    }

    @NotNull
    private static SliceImpl<ProfileResponse> ToSliceProfileImpl(Pageable pageable, List<Profile> profileList) {
        boolean hasNext = false;

        if (profileList.size() > pageable.getPageSize()) {
            profileList.remove(profileList.size() - 1);
            hasNext = true;
        }

        if (profileList.size() == 0) {
            return new SliceImpl<>(new ArrayList<>(), pageable, hasNext);
        }

        List<ProfileResponse> profileResponses = profileList.stream()
                .map(ProfileResponse::of)
                .toList();

        return new SliceImpl<>(profileResponses, pageable, hasNext);
    }
}
