package io.wisoft.wasabi.global.exception;

import io.wisoft.wasabi.global.config.web.response.CommonResponse;
import io.wisoft.wasabi.global.config.web.response.dto.error.ErrorDataResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CommonResponse> handlerMethodArgumentNotValidException(final MethodArgumentNotValidException ex) {
        final List<String> globalErrors = ex.getGlobalErrors().stream()
                .map(ObjectError::getDefaultMessage)
                .toList();

        final List<String> fieldErrors = ex.getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .toList();

        final List<String> allErrors = new ArrayList<>();
        allErrors.addAll(globalErrors);
        allErrors.addAll(fieldErrors);

        final String message = String.join(", ", allErrors);

        return buildResponse(ErrorType.dtoInvalid(message));
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<CommonResponse> handleBusinessException(final BusinessException ex) {
        return buildResponse(ex.getErrorType());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<CommonResponse> handleRuntimeException(final RuntimeException ex) {
        return buildResponse(ErrorType.UNCAUGHT_ERROR);
    }

    private ResponseEntity<CommonResponse> buildResponse(final ErrorType errorType) {
        final ErrorDataResponse errorDataResponse = ErrorDataResponse.newInstance(errorType);
        final CommonResponse response = CommonResponse.newInstance(errorDataResponse);

        return ResponseEntity.status(errorType.getHttpStatusCode()).body(response);
    }
}
