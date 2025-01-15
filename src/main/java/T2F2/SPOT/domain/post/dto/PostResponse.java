package T2F2.SPOT.domain.post.dto;

import T2F2.SPOT.domain.post.PostStatus;
import T2F2.SPOT.domain.post.entity.Post;
import T2F2.SPOT.domain.user.entity.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
public class PostResponse {

    private final Long postId;
    private final String title;
    private final String content;
    private final String userNickname;
    private final Long writerId;
    private final PostStatus postStatus;
    private final int price;
    private final int wish_count;
    private final LocalDateTime post_date;
    private final int viewCount;
    private final String firstImageUrl;

    public PostResponse(Long postId, String title, String content, User user, PostStatus postStatus, int price, int wish_count, LocalDateTime post_date, int viewCount, String firstImageUrl) {
        this.postId = postId;
        this.title = title;
        this.content = content;
        this.userNickname = user.getNickname();
        this.writerId = user.getId();
        this.postStatus = postStatus;
        this.price = price;
        this.wish_count = wish_count;
        this.post_date = post_date;
        this.viewCount = viewCount;
        this.firstImageUrl = firstImageUrl;
    }

    public static PostResponse of(Post post, String firstImageUrl) {

        return new PostResponse(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getUser(),
                post.getPostStatus(),
                post.getPrice(),
                post.getWishes().size(),
                post.getCreatedDate(),
                post.getViewCount(),
                firstImageUrl
        );
    }
}