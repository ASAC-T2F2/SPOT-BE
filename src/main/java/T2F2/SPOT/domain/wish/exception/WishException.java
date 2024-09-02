package T2F2.SPOT.domain.wish.exception;

public class WishException {

    public static class WishNotFoundException extends RuntimeException {
        public WishNotFoundException(String message) {
            super(message);
        }
    }
}
