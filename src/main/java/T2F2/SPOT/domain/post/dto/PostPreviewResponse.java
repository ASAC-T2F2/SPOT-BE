package T2F2.SPOT.domain.post.dto;

import T2F2.SPOT.domain.post.entity.Post;
import lombok.Getter;

@Getter
public class PostPreviewResponse {
    private final Long postId;
    private final String title;
    private final int price;
    private final Long writerId;
    private final String writerName;

    private PostPreviewResponse(Long postId, String title, int price, Long writerId, String writerName) {
        this.postId = postId;
        this.title = title;
        this.price = price;
        this.writerId = writerId;
        this.writerName = writerName;
    }

    public static PostPreviewResponse of(Post post) {
        return new PostPreviewResponse(post.getId(), post.getTitle(), post.getPrice(), post.getUser().getId(), post.getUser().getNickname());
    }


}