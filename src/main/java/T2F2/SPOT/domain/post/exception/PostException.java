package T2F2.SPOT.domain.post.exception;

public class PostException {

    /**
     * 게시글이 존재하지 않는 예외
     */
    public static class PostNotFoundException extends RuntimeException {
        public PostNotFoundException(String message) {
            super(message);
        }
    }
}
