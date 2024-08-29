package T2F2.SPOT.domain.wish.dto;

import T2F2.SPOT.domain.wish.entity.Wish;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CancelWishResponse {

    private Long wishId;
    private Long userId;
    private Long postId;

    public static CancelWishResponse fromWish(Wish wish) {
        return new CancelWishResponse(
                wish.getId(),
                wish.getUser().getId(),
                wish.getPost().getId());
    }
}
