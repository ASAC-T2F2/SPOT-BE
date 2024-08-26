package T2F2.SPOT.domain.review.dto;

import T2F2.SPOT.domain.review.entity.Review;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ReviewResponse {

    private Long reviewId;
    private Long senderId;
    private Long receiverId;
    private Long postId;
    private float rate;
    private String message;


    public static ReviewResponse from(Review review) {
        return new ReviewResponse(
                review.getId(),
                review.getSender().getId(),
                review.getReceiver().getId(),
                review.getPost().getId(),
                review.getRate(),
                review.getMessage()
        );
    }
}
