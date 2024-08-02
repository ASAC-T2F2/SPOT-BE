package T2F2.SPOT.domain.post;

import T2F2.SPOT.domain.category.entity.Category;
import T2F2.SPOT.domain.post.dto.SearchPostConditionDto;
import T2F2.SPOT.domain.post.entity.Post;
import T2F2.SPOT.domain.post.entity.QPost;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;

import static T2F2.SPOT.domain.post.entity.QPost.post;

public class PostQueryHelper {
    /**
     * 정렬 수행
     * @param sortBy 정렬 조건
     * @param post
     * @return
     */
    public static OrderSpecifier<?> getOrderSpecifier(SortBy sortBy, QPost post) {
        if (sortBy == null) {
            // sort가 null인 경우 기본 정렬 기준으로 처리
            return post.createdDate.desc();
        }
        switch (sortBy) {
            case POPULAR:
                return post.wishes.size().desc();
            case LATEST:
            default:
                return post.createdDate.desc();
        }
    }

    public static BooleanBuilder createFilterBuilder(SearchPostConditionDto searchPostConditionDto){
        BooleanBuilder condition = new BooleanBuilder();
        addCatecory(searchPostConditionDto.getCategory(), condition, post);
        addPostCondition(searchPostConditionDto.getPostFor(), searchPostConditionDto.getPostStatus(), searchPostConditionDto.getMinPrice(), searchPostConditionDto.getMaxPrice(),
                        condition, post
        );
        addKeywordFilter(searchPostConditionDto.getKeyword(), condition, post);

        return condition;
    }

    private static void addPostCondition(PostFor postFor, PostStatus postStatus, String minPrice, String maxPrice,
                                         BooleanBuilder condition, QPost post) {
        if(postFor != null) {
            condition.and(
                    post.postFor.eq(postFor)
            );
        }
        if(postStatus != null) {
            condition.and(
                    post.postStatus.eq(postStatus)
            );
        }

    }
    // 카테고리 분류
    private static void addCatecory(Category category, BooleanBuilder condition, QPost post){
        if (category != null) {
            condition.andAnyOf(
                    post.category.categoryName.eq(category.getCategoryName())
            );
        }
    }
    // 검색 메서드
    private static void addKeywordFilter(String keyword, BooleanBuilder condition, QPost post) {
        if (keyword != null) {
            condition.and(
                    post.title.containsIgnoreCase(keyword)
                            .or(post.content.containsIgnoreCase(keyword))
            );
        }
    }

}
