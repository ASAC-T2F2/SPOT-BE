package T2F2.SPOT.domain.post.dto;

import lombok.Getter;
import java.util.List;

@Getter
public class PostListWithPagination {

    private final List<PostResponse> posts;
    private final Long lastId;
    private final boolean hasMore;

    private PostListWithPagination(List<PostResponse> posts, Long lastId, boolean hasMore) {
        this.posts = posts;
        this.lastId = lastId;
        this.hasMore = hasMore;
    }

    public static PostListWithPagination of(List<PostResponse> posts, Long lastId, boolean hasMore) {
        return new PostListWithPagination(posts, lastId, hasMore);
    }
}
