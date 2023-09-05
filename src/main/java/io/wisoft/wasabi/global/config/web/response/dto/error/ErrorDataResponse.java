package io.wisoft.wasabi.global.config.web.response.dto.error;

import io.wisoft.wasabi.global.config.web.response.ResponseType;

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

    public static ErrorDataResponse newInstance(final ResponseType responseType) {
        ErrorDataResponse dataResponse = new ErrorDataResponse();
        dataResponse.errorCode = responseType.getCode();
        dataResponse.errorMessage = responseType.getMessage();
        return dataResponse;
    }

}
