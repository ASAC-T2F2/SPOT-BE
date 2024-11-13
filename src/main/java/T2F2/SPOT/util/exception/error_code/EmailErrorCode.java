package T2F2.SPOT.util.exception.error_code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum EmailErrorCode implements ErrorCode {

    NOT_FOUND(HttpStatus.NOT_FOUND, "해당 이메일을 찾을 수 없습니다."),
    INVALID_VERIFICATION_CODE(HttpStatus.UNAUTHORIZED, "인증코드가 일치하지 않습니다."),
    EXPIRED_VERIFICATION_CODE(HttpStatus.GONE, "인증 코드가 만료되었습니다."),
    SEND_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "이메일 전송에 실패했습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
