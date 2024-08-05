package T2F2.SPOT.domain.post.repository;

import T2F2.SPOT.domain.post.dto.QPostDto;
import T2F2.SPOT.domain.post.dto.SearchPostConditionDto;
import T2F2.SPOT.domain.post.entity.Post;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static T2F2.SPOT.domain.post.PostQueryHelper.createFilterBuilder;
import static T2F2.SPOT.domain.post.PostQueryHelper.getOrderSpecifier;
import static T2F2.SPOT.domain.post.entity.QPost.post;

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
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch()
                .stream()
                .map(post -> post.getIsDeleted() ? null : post)
                .filter(Objects::nonNull)
                .toList();

        List<QPostDto> content = posts.stream().map(QPostDto::of).toList();
        return new PageImpl<>(content);

    }

}
