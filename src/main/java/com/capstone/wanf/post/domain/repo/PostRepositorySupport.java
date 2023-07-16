package com.capstone.wanf.post.domain.repo;

import com.capstone.wanf.post.domain.entity.Category;
import com.capstone.wanf.post.domain.entity.Post;
import com.capstone.wanf.post.domain.entity.QPost;
import com.capstone.wanf.post.dto.response.PostPaginationResponse;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class PostRepositorySupport {
    private final JPAQueryFactory jpaQueryFactory;

    private final QPost post = QPost.post;

    public Slice<PostPaginationResponse> findAll(Category category, Pageable pageable) {
        List<Post> postList = jpaQueryFactory.selectFrom(post)
                .where(new BooleanExpression[]{post.category.eq(category)})
                .limit(pageable.getPageSize() + 1)
                .offset(pageable.getOffset())
                .orderBy(getOrderSpecifiers(pageable.getSort()))
                .fetch();

        return ToSlicePostImpl(pageable, postList);
    }
    
    public List<PostPaginationResponse> findAll(Category category) {
        List<Post> postList = jpaQueryFactory.selectFrom(post)
                .where(new BooleanExpression[]{post.category.eq(category)})
                .fetch();

        List<PostPaginationResponse> postPaginationResponses = toPostPaginationResponse(postList);

        return postPaginationResponses;
    }

    public Slice<PostPaginationResponse> searchAllByQuery(Category category, String query, Pageable pageable) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();

        booleanBuilder
                .and(post.category.eq(category))
                .and(post.course.name.contains(query));

        List<Post> searchedPosts = jpaQueryFactory.selectFrom(post)
                .where(booleanBuilder)
                .limit(pageable.getPageSize() + 1)
                .offset(pageable.getOffset())
                .orderBy(getOrderSpecifiers(pageable.getSort()))
                .fetch();

        return ToSlicePostImpl(pageable, searchedPosts);
    }

    public OrderSpecifier<?>[] getOrderSpecifiers(Sort sort) {
        List<OrderSpecifier<?>> orderSpecifiers = new ArrayList<>();

        for (Sort.Order order : sort) {
            Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;

            OrderSpecifier<?> orderSpecifier = null;

            switch (order.getProperty()) {
                case "id" -> orderSpecifier = new OrderSpecifier<>(direction, post.id);
                case "modifiedDate" -> orderSpecifier = new OrderSpecifier<>(direction, post.modifiedDate);
            }

            if (orderSpecifier != null) {
                orderSpecifiers.add(orderSpecifier);
            }
        }

        if (!orderSpecifiers.isEmpty()) {
            return orderSpecifiers.toArray(new OrderSpecifier<?>[0]);
        } else {
            return new OrderSpecifier<?>[0];
        }
    }

    @NotNull
    private static List<PostPaginationResponse> toPostPaginationResponse(List<Post> postList) {
        return postList.stream()
                .map(Post::toPostPaginationResponse)
                .collect(Collectors.toList());
    }

    @NotNull
    private static SliceImpl<PostPaginationResponse> ToSlicePostImpl(Pageable pageable, List<Post> postList) {
        boolean hasNext = false;

        if(postList.size() > pageable.getPageSize()) {
            postList.remove(postList.size() - 1);
            hasNext = true;
        }

        if(postList.size() == 0) {
            return new SliceImpl<>(new ArrayList<>(), pageable, hasNext);
        }

        List<PostPaginationResponse> postPaginationResponses = toPostPaginationResponse(postList);

        return new SliceImpl<>(postPaginationResponses, pageable, hasNext);
    }
}
