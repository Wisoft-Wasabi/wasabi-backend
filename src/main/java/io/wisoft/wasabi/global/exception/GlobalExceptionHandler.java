package io.wisoft.wasabi.global.exception;

import io.wisoft.wasabi.domain.member.exception.MemberNotFoundException;
import io.wisoft.wasabi.global.response.CommonResponse;
import io.wisoft.wasabi.global.response.dto.error.ErrorDataResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleNotFoundException(RuntimeException ex) {
        // 예외 처리 로직
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(MemberNotFoundException.class)
    public ResponseEntity<CommonResponse> memberNotFound(MemberNotFoundException ex) {
        // 예외 처리 로직
        ErrorDataResponse errorDataResponse = ErrorDataResponse.newInstance(ex.getErrorType());
        CommonResponse response = CommonResponse.newInstance(errorDataResponse);

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

}

