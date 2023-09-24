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
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ProfileRepositorySupport {
    private final JPAQueryFactory jpaQueryFactory;

    private final QProfile profile = QProfile.profile;

    public Slice<ProfileResponse> findByRandom(Pageable pageable, Long myProfileId) {
        // querydsl은 random 함수를 지원하지 않음. 따라서 native query를 사용해야 함
        // MySQL은 NumberExpression을 지원 X. 따라서 Expressions.numberTemplate을 사용
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
        boolean hasNext = false;        // 다음 페이지 존재 여부

        // 조회된 데이터가 요청한 페이지 사이즈보다 큰 경우
        if (profileList.size() > pageable.getPageSize()) {
            profileList.remove(profileList.size() - 1);
            hasNext = true;
        }

        if (profileList.size() == 0) {  // 조회된 데이터가 없는 경우
            return new SliceImpl<>(new ArrayList<>(), pageable, hasNext);  // 빈 리스트 반환
        }

        // profileList를 profileResponseList로 변환
        List<ProfileResponse> profileResponses = profileList.stream()
                .map(Profile::toResponse)
                .collect(Collectors.toList());

        return new SliceImpl<>(profileResponses, pageable, hasNext);    // SliceImpl 객체 반환
    }
}
