package io.wisoft.wasabi.global.exception;

import io.wisoft.wasabi.global.response.CommonResponse;
import io.wisoft.wasabi.global.response.dto.error.ErrorDataResponse;
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
        final ResponseEntity response = buildResponse(ErrorType.dtoInvalid(message));

        return response;

    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<CommonResponse> handleBusinessException(final BusinessException ex) {
        final ResponseEntity response = buildResponse(ex.getErrorType());
        return response;
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<CommonResponse> handleRuntimeException(final RuntimeException ex) {
        final ResponseEntity response = buildResponse(ErrorType.UNCAUGHT_ERROR);
        return response;
    }
  
    private ResponseEntity buildResponse(final ErrorType errorType) {
        final ErrorDataResponse errorDataResponse = ErrorDataResponse.newInstance(errorType);
        final CommonResponse response = CommonResponse.newInstance(errorDataResponse);

        return ResponseEntity.status(errorType.getHttpStatusCode()).body(response);
    }
}
