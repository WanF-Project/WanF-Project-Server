package com.capstone.wanf.post.domain.repo;

import com.capstone.wanf.post.domain.entity.Category;
import com.capstone.wanf.post.domain.entity.Post;
import com.capstone.wanf.post.domain.entity.QPost;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class PostRepositorySupport {
    private final JPAQueryFactory jpaQueryFactory;

    private final QPost post = QPost.post;

    public Slice<Post> findAll(Category category, Pageable pageable) {
        List<Post> postList = jpaQueryFactory.selectFrom(post)
                .where(new BooleanExpression[]{post.category.eq(category)})
                .limit(pageable.getPageSize() + 1)
                .offset(pageable.getOffset())
                .orderBy(getOrderSpecifiers(pageable.getSort()))
                .fetch();

        boolean hasNext = false;

        if(postList.size() > pageable.getPageSize()) {
            postList.remove(postList.size() - 1);
            hasNext = true;
        }

        return new SliceImpl<>(postList, pageable, hasNext);
    }

    public List<Post> findAll(Category category) {
        List<Post> postList = jpaQueryFactory.selectFrom(post)
                .where(new BooleanExpression[]{post.category.eq(category)})
                .fetch();

        return postList;
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
}
