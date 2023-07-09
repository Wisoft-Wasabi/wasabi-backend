package io.wisoft.wasabi.global.response.dto.error;

import io.wisoft.wasabi.global.exception.ErrorType;
import io.wisoft.wasabi.global.response.dto.DataResponse;

public class ErrorDataResponse implements DataResponse {
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

    public static ErrorDataResponse newInstance(ErrorType errorType) {
        ErrorDataResponse dataResponse = new ErrorDataResponse();
        dataResponse.errorCode = errorType.getErrorCode();
        dataResponse.errorMessage = errorType.getErrorMessage();
        return dataResponse;
    }


}
