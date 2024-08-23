package T2F2.SPOT.domain.post.repository;

import T2F2.SPOT.domain.post.dto.QPostDto;
import T2F2.SPOT.domain.post.dto.SearchPostConditionDto;
import T2F2.SPOT.domain.post.entity.Post;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

import static T2F2.SPOT.domain.post.PostQueryHelper.createFilterBuilder;
import static T2F2.SPOT.domain.post.PostQueryHelper.getOrderSpecifier;
import static T2F2.SPOT.domain.post.entity.QPost.post;
import static T2F2.SPOT.domain.user.entity.QUser.user;

@Slf4j
@Repository
@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom{

    private final JPAQueryFactory queryFactory;


    @Override
    public Slice<QPostDto> searchPosts(Pageable pageable, SearchPostConditionDto searchPostConditionDto) {

        BooleanBuilder condition = createFilterBuilder(searchPostConditionDto);

        log.info("condition : {} ", condition );
        List<Post> posts = queryFactory
                .selectFrom(post)
                .where(condition)
                .orderBy(getOrderSpecifier(searchPostConditionDto.getSortBy(), post))
//                .offset(pageable.getOffset())
//                .limit(pageable.getPageSize())
                .fetch()
                .stream()
                .map(post -> post.getIsDeleted() ? null : post)
                .filter(Objects::nonNull)
                .toList();

        List<QPostDto> content = posts.stream().map(QPostDto::of).toList();
        return new PageImpl<>(content);

    }

    @Override
    public List<Post> findByMajor(String major) {
        return queryFactory
                .selectFrom(post)
                .join(post.user, user)
                .where(user.major.eq(major))
                .fetch();
    }

}