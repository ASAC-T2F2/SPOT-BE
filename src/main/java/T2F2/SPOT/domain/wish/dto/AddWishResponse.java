package T2F2.SPOT.domain.wish.dto;

import T2F2.SPOT.domain.post.entity.Post;
import T2F2.SPOT.domain.user.entity.User;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AddWishResponse {
    private Long targetPostId;
    private Long userId;

    public static AddWishResponse fromPost(Post post, User user) {
        return new AddWishResponse(post.getId(),user.getId());
    }
}
