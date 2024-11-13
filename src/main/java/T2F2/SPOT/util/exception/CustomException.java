package T2F2.SPOT.util.exception;

import T2F2.SPOT.util.exception.error_code.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CustomException extends RuntimeException {

    private final ErrorCode errorCode;
}
