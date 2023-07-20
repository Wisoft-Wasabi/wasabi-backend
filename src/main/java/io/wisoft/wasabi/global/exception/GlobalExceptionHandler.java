package io.wisoft.wasabi.global.exception;

import io.wisoft.wasabi.global.response.CommonResponse;
import io.wisoft.wasabi.global.response.dto.error.ErrorDataResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<CommonResponse> handleBusinessException(final BusinessException ex) {
        // 예외 처리 로직
        final ResponseEntity response = buildResponse(ex.getErrorType());
        return response;
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<CommonResponse> handleRuntimeException(final RuntimeException ex) {
        // 예외 처리 로직
        final ResponseEntity response = buildResponse(ErrorType.UNCAUGHT_ERROR);
        return response;
    }

    private ResponseEntity buildResponse(final ErrorType errorType) {
        final ErrorDataResponse errorDataResponse = ErrorDataResponse.newInstance(errorType);
        final CommonResponse response = CommonResponse.newInstance(errorDataResponse);

        return ResponseEntity.status(errorType.getHttpStatusCode()).body(response);
    }
}
