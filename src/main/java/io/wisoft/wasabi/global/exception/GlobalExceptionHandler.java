package io.wisoft.wasabi.global.exception;

import io.wisoft.wasabi.domain.auth.exception.PasswordInvalidException;
import io.wisoft.wasabi.domain.auth.exception.SigninFailException;
import io.wisoft.wasabi.domain.member.exception.MemberEmailOverlapException;
import io.wisoft.wasabi.domain.member.exception.MemberNotFoundException;
import io.wisoft.wasabi.global.response.CommonResponse;
import io.wisoft.wasabi.global.response.dto.error.ErrorDataResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(MemberNotFoundException.class)
    public ResponseEntity<CommonResponse> memberNotFound(MemberNotFoundException ex) {
        ErrorType errorType = ex.getErrorType();
        ErrorDataResponse errorDataResponse = ErrorDataResponse.newInstance(errorType);
        CommonResponse response = CommonResponse.newInstance(errorDataResponse);

        return ResponseEntity.status(errorType.getHttpStatusCode()).body(response);
    }

    @ExceptionHandler(MemberEmailOverlapException.class)
    public ResponseEntity<CommonResponse> memberEmailOverlap(MemberEmailOverlapException ex) {
        // 예외 처리 로직
        ErrorType errorType = ex.getErrorType();
        ErrorDataResponse errorDataResponse = ErrorDataResponse.newInstance(errorType);
        CommonResponse response = CommonResponse.newInstance(errorDataResponse);

        return ResponseEntity.status(errorType.getHttpStatusCode()).body(response);
    }

    @ExceptionHandler(PasswordInvalidException.class)
    public ResponseEntity<CommonResponse> passwordInvalid(PasswordInvalidException ex) {
        // 예외 처리 로직
        ErrorType errorType = ex.getErrorType();
        ErrorDataResponse errorDataResponse = ErrorDataResponse.newInstance(errorType);
        CommonResponse response = CommonResponse.newInstance(errorDataResponse);

        return ResponseEntity.status(errorType.getHttpStatusCode()).body(response);
    }

    @ExceptionHandler(SigninFailException.class)
    public ResponseEntity<CommonResponse> signinFail(SigninFailException ex) {
        // 예외 처리 로직
        ErrorType errorType = ex.getErrorType();
        ErrorDataResponse errorDataResponse = ErrorDataResponse.newInstance(errorType);
        CommonResponse response = CommonResponse.newInstance(errorDataResponse);

        return ResponseEntity.status(errorType.getHttpStatusCode()).body(response);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<CommonResponse> handleNotFoundException(RuntimeException ex) {
        // 예외 처리 로직
        ErrorType errorType = ErrorType.UNCAUGHT_ERROR;
        ErrorDataResponse errorDataResponse = ErrorDataResponse.newInstance(errorType);
        CommonResponse response = CommonResponse.newInstance(errorDataResponse);

        return ResponseEntity.status(errorType.getHttpStatusCode()).body(response);
    }

}

