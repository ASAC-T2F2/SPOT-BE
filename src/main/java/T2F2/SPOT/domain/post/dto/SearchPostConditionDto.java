package T2F2.SPOT.domain.post.dto;

import T2F2.SPOT.domain.category.entity.Category;
import T2F2.SPOT.domain.post.PostFor;
import T2F2.SPOT.domain.post.PostStatus;
import T2F2.SPOT.domain.post.SortBy;
import lombok.Getter;

@Getter
public class SearchPostConditionDto {
    String keyword;
    Category category;
    PostFor postFor;
    PostStatus postStatus;
    String minPrice;
    String maxPrice;
    SortBy sortBy;

    private SearchPostConditionDto(String keyword, Category category, PostFor postFor, PostStatus postStatus,
                                   String minPrice, String maxPrice, SortBy sortBy) {
        this.keyword = keyword;
        this.category = category;
        this.postFor = postFor;
        this.postStatus = postStatus;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.sortBy = sortBy;
    }

    public static SearchPostConditionDto of(
            String keyword, Category category, PostFor postFor, PostStatus postStatus,
            String minPrice, String maxPrice, SortBy sortBy) {
        return new SearchPostConditionDto(keyword, category, postFor, postStatus, minPrice, maxPrice, sortBy);
    }
}
