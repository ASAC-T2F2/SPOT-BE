package T2F2.SPOT.domain.user.exception;

public class TokenException {

    public static class RefreshTokenIsNullException extends RuntimeException {
        public RefreshTokenIsNullException(String message) {
            super(message);
        }
    }

    public static class RefreshTokenExpiredException extends RuntimeException {
        public RefreshTokenExpiredException(String message) {
            super(message);
        }
    }

}
