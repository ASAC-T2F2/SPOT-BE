package T2F2.SPOT.domain.review.exception;

public class ReviewException {

    /**
     * 리뷰가 이미 존재
     */
    public static class ReviewAlreadyExist extends RuntimeException {
        public ReviewAlreadyExist(String message) {
            super(message);
        }
    }
}
