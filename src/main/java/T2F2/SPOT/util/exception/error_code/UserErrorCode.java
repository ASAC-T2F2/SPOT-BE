package T2F2.SPOT.util.exception.error_code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserErrorCode implements ErrorCode {

    SIGN_UP_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "회원가입 중 문제가 발생하였습니다."),
    EMAIL_ALREADY_EXIST(HttpStatus.CONFLICT, "이미 존재하는 이메일입니다."),
    NICKNAME_ALREADY_EXIST(HttpStatus.CONFLICT, "이미 존재하는 닉네임입니다."),
    NOT_FOUND(HttpStatus.NOT_FOUND, "해당 사용자를 찾을 수 없습니다."),
    SAME_PASSWORD_INPUT(HttpStatus.BAD_REQUEST, "기존 비밀번호와 동일합니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
