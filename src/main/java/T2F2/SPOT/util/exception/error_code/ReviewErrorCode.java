package T2F2.SPOT.util.exception.error_code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ReviewErrorCode implements ErrorCode {

    REVIEW_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 리뷰를 작성했습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
