package T2F2.SPOT.domain.post.repository;

import T2F2.SPOT.domain.post.PostFor;
import T2F2.SPOT.domain.post.dto.PostPreviewResponse;
import T2F2.SPOT.domain.post.dto.SearchPostConditionDto;
import T2F2.SPOT.domain.post.entity.Post;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import java.util.List;

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
    public Slice<PostPreviewResponse> searchPosts(Pageable pageable, SearchPostConditionDto searchPostConditionDto) {

        BooleanBuilder condition = createFilterBuilder(searchPostConditionDto);

        log.info("condition : {} ", condition );

        List<Post> posts = queryFactory
                .selectFrom(post)
                .join(post.user, user).fetchJoin()
                .where(condition)
                .orderBy(getOrderSpecifier(searchPostConditionDto.getSortBy(), post))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1) // 요청 개수보다 1개 더 가져옴
                .fetch();

        List<PostPreviewResponse> content = posts.stream()
                .filter(post -> !post.getIsDeleted())
                .map(PostPreviewResponse::of)
                .toList();

        // 초과 데이터 확인 및 제거
        boolean hasNext = content.size() > pageable.getPageSize();
        if (hasNext) {
            content = content.subList(0, pageable.getPageSize());
        }

        return new SliceImpl<>(content, pageable, hasNext);
    }

    @Override
    public List<Post> findByMajor(String major) {
        return queryFactory
                .selectFrom(post)
                .join(post.user, user)
                .where(user.major.eq(major))
                .fetch();
    }

    @Override
    public List<Post> findByUserId(Long userId) {
        return queryFactory
                .selectFrom(post)
                .join(post.user, user)
                .where(user.id.eq(userId))
                .fetch();
    }


    /**
     * 살래요/팔래요 게시글 목록 가져오기 (무한스크롤)
     * @param limit
     * @param lastPostId
     * @param postFor
     * @return 목적에 맞는 게시글 목록
     */
    @Override
    public List<Post> fetchPostsForPurposeSorted(int limit, Long lastPostId, PostFor postFor) {
        OrderSpecifier<?> newest = post.createdDate.desc();

        return queryFactory
                .selectFrom(post)
                .join(post.user, user)
                .where(post.postFor.eq(postFor)
                        .and(post.id.lt(lastPostId)))
                .orderBy(newest)
                .limit(limit)
                .fetch();
    }

    /**
     * 가져올 게시글이 더 남았는지 확인.
     * postId와 작성시간의 순서가 같으므로 정렬은 생략.
     * @param lastPostId
     * @param postFor
     * @return 참/거짓
     */
    @Override
    public boolean hasMorePosts(Long lastPostId, PostFor postFor) {
        return queryFactory
                .selectOne()
                .from(post)
                .where(post.postFor.eq(postFor)
                        .and(post.id.lt(lastPostId)))
                .fetchFirst() != null;
    }
}