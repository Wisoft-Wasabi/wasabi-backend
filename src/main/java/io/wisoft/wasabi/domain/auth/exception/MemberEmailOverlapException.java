package io.wisoft.wasabi.domain.member.exception;

import io.wisoft.wasabi.global.exception.BusinessException;
import io.wisoft.wasabi.global.exception.ErrorType;
import io.wisoft.wasabi.global.response.CommonResponse;
import io.wisoft.wasabi.global.response.dto.error.ErrorDataResponse;
import org.springframework.http.ResponseEntity;

public class MemberEmailOverlapException extends BusinessException {
    public MemberEmailOverlapException() {
        super(ErrorType.MEMBER_EMAIL_OVERLAP.getErrorMessage());
    }

    @Override
    protected ResponseEntity<CommonResponse> buildResponse(ErrorType errorType) {
        final ErrorDataResponse errorDataResponse = ErrorDataResponse.newInstance(errorType);
        final CommonResponse response = CommonResponse.newInstance(errorDataResponse);
        return ResponseEntity.status(errorType.getHttpStatusCode()).body(response);
    }

    @Override
    public ErrorType getErrorType() {
        return ErrorType.MEMBER_EMAIL_OVERLAP;
    }
}
