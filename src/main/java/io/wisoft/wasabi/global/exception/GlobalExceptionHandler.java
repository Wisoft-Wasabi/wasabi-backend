package io.wisoft.wasabi.global.exception;

import io.wisoft.wasabi.global.response.CommonResponse;
import io.wisoft.wasabi.global.response.dto.error.ErrorDataResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<CommonResponse> handleBusinessException(final BusinessException ex) {
        // 예외 처리 로직
        ErrorType errorType = ex.getErrorType();
        ErrorDataResponse errorDataResponse = ErrorDataResponse.newInstance(errorType);
        CommonResponse response = CommonResponse.newInstance(errorDataResponse);

        return ResponseEntity.status(errorType.getHttpStatusCode()).body(response);
    }


    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<CommonResponse> handleRuntimeException(RuntimeException ex) {
        // 예외 처리 로직
        ErrorType errorType = ErrorType.UNCAUGHT_ERROR;
        ErrorDataResponse errorDataResponse = ErrorDataResponse.newInstance(errorType);
        CommonResponse response = CommonResponse.newInstance(errorDataResponse);

        return ResponseEntity.status(errorType.getHttpStatusCode()).body(response);
    }

}

