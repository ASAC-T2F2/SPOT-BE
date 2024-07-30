package T2F2.SPOT.domain.user.exception;

public class UserExceptions {

    public static class UserSignUpException extends RuntimeException {
        public UserSignUpException(String message) {
            super(message);
        }
    }

    public static class EmailAlreadyExistsException extends UserSignUpException {
        public EmailAlreadyExistsException(String message) {
            super(message);
        }
    }

    public static class SignUpFailedException extends UserSignUpException {
        public SignUpFailedException(String message) {
            super(message);
        }
    }
}
