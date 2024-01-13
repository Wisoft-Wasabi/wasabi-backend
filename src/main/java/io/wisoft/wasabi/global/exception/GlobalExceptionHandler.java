package io.wisoft.wasabi.global.exception;

import io.wisoft.wasabi.global.config.web.response.Response;
import io.wisoft.wasabi.global.config.web.response.ResponseType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Response<?>> handlerMethodArgumentNotValidException(final MethodArgumentNotValidException ex) {
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

        logger.info("\n [Error] MethodArgumentNotValidException : ErrorMessage : {}", message);

        return buildResponse(ResponseType.dtoInvalid(message));
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Response<?>> handleBusinessException(final BusinessException ex) {

        logger.info("\n [Error] BusinessException : HttpStatus : {}, ErrorCode : {}, ErrorType : {}", ex.getErrorType().getStatus(), ex.getErrorType().getCode(), ex.getErrorType());

        return buildResponse(ex.getErrorType());
    }

//    @ExceptionHandler(RuntimeException.class)
//    public ResponseEntity<Response<?>> handleRuntimeException(final RuntimeException ex) {
//
//        logger.info("\n [Error] RuntimeException : ErrorMessage : {}  Path: {}", ex.getMessage(), ex.fillInStackTrace());
//
//        return buildResponse(ResponseType.UNCAUGHT_ERROR);
//    }

    private ResponseEntity<Response<?>> buildResponse(final ResponseType responseType) {
        return ResponseEntity.ofNullable(
                Response.of(
                        responseType,
                        null
                )
        );
    }

}
