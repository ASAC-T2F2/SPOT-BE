package T2F2.SPOT.domain.email.exception;

public class EmailException {

    /**
     * 인증번호 틀림
     */
    public static class InvalidVerificationCodeException extends RuntimeException {
        public InvalidVerificationCodeException(String message) {
            super(message);
        }
    }

    /**
     * 인증시간 만료
     */
    public static class ExpiredVerificationCodeException extends RuntimeException {
        public ExpiredVerificationCodeException(String message) {
            super(message);
        }
    }
}
