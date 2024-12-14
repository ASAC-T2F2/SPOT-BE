package T2F2.SPOT.domain.post.dto;

import T2F2.SPOT.domain.post.entity.Post;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PostDetail extends PostResponse {

    @JsonProperty("isAuthor")
    private final boolean isAuthor;

    private PostDetail(Post post, boolean isAuthor) {
        super(
                post.getTitle(),
                post.getContent(),
                post.getUser(),
                post.getPostStatus(),
                post.getPrice(),
                post.getWishes().size(),
                post.getCreatedDate(),
                post.getViewCount()
        );
        this.isAuthor = isAuthor;
    }

    public static PostDetail of(Post post, boolean isAuthor) {
        return new PostDetail(post, isAuthor);
    }

    // @Getter와 @JasonProperty의 충돌 + isAuthor 필드명 이슈로 Getter 직접 작성
    public boolean getIsAuthor() {
        return isAuthor;
    }
}
