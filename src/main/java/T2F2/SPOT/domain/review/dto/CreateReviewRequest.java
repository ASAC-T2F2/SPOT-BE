package T2F2.SPOT.domain.review.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateReviewRequest {

    private Long postId;
    private Long receiverId;

    private float rate;
    private String message;
}
