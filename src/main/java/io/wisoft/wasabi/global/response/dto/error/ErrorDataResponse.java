package io.wisoft.wasabi.global.response.dto.error;

import io.wisoft.wasabi.global.exception.ErrorType;

public class ErrorDataResponse {
    private String errorCode;
    private String errorMessage;

    protected ErrorDataResponse() {
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public static ErrorDataResponse newInstance(final ErrorType errorType) {
        ErrorDataResponse dataResponse = new ErrorDataResponse();
        dataResponse.errorCode = errorType.getErrorCode();
        dataResponse.errorMessage = errorType.getErrorMessage();
        return dataResponse;
    }

}
