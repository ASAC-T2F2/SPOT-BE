package T2F2.SPOT.util.exception.error_code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum NoteErrorCode implements ErrorCode {

    NOT_FOUND(HttpStatus.NOT_FOUND, "해당 채팅방을 찾을 수 없습니다."),
    DELETE_NOT_AUTHORIZED(HttpStatus.UNAUTHORIZED, "채팅방을 삭제할 권한이 없습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
