package T2F2.SPOT.domain.user.exception;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserExceptions {

    /**
     * 회원가입 예외
     */
    public static class UserSignUpException extends RuntimeException {
        public UserSignUpException(String message) {
            super(message);
        }
    }

    /**
     * 이메일이 이미 존재
     */
    public static class EmailAlreadyExistsException extends UserSignUpException {
        public EmailAlreadyExistsException(String message) {
            super(message);
        }
    }

    /**
     * 회원가입 실패
     */
    public static class SignUpFailedException extends UserSignUpException {
        public SignUpFailedException(String message) {
            super(message);
        }
    }

    /**
     * User 획득 불가
     */
    public static class UserNotFoundException extends UsernameNotFoundException {
        public UserNotFoundException(String message) {
            super(message);
        }
    }
}
