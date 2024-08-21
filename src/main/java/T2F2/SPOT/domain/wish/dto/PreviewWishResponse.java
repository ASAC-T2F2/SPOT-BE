package T2F2.SPOT.domain.wish.dto;

import T2F2.SPOT.domain.post.entity.Post;
import T2F2.SPOT.domain.user.entity.User;
import T2F2.SPOT.domain.wish.entity.Wish;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PreviewWishResponse {

    private Long wishId;
    private Long postId;

    private String image;
    private String title;
    private String content;
    private String writer;
    private String price;

    public static PreviewWishResponse fromWish(Wish wish, Post post) {
        return new PreviewWishResponse(
                wish.getId(),
                post.getId(),
                post.getFirstImageOrDefault(),
                post.getPostTitle(),
                post.getPostContent(),
                post.getUser().getNickname(),
                post.getPrice()
        );
    }
}
