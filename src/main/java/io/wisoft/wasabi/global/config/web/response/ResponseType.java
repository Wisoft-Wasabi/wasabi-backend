package io.wisoft.wasabi.global.config.web.response;


import org.springframework.http.HttpStatus;

public enum ResponseType {

    /* 인증 - AUTH */
    SIGN_UP_SUCCESS(HttpStatus.CREATED, "AUTH-S001", "Sign up Success"),
    LOGIN_SUCCESS(HttpStatus.OK, "AUTH-S002", "Login Success"),

    LOGIN_FAIL(HttpStatus.BAD_REQUEST, "AUTH-F001", "Login Fail"),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "AUTH-F002", "Token Not Exist"),
    FORBIDDEN(HttpStatus.FORBIDDEN, "AUTH-F003", "Member Not Activated"),

    /* 회원 - MEMBER */
    MEMBER_UPDATE_INFO_SUCCESS(HttpStatus.OK, "MEMBER-S001", "Member Update Success"),
    READ_MEMBER_INFO_SUCCESS(HttpStatus.OK, "MEMBER-S002", "Member Info Read Success"),
    MEMBER_APPROVE_SUCCESS(HttpStatus.OK, "MEMBER-S003", "Member Approve Success"),
    READ_MEMBER_UN_APPROVE_SUCCESS(HttpStatus.OK, "MEMBER-S004", "Read Member Unapprove Success"),
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "MEMBER-F001", "User Not Found"),
    MEMBER_EMAIL_OVERLAP(HttpStatus.CONFLICT, "MEMBER-F003", "User Email Overlap"),

    /* 게시글 - BOARD */
    BOARD_WRITE_SUCCESS(HttpStatus.CREATED, "BOARD-S001", "Board Write Success"),
    BOARD_READ_SUCCESS(HttpStatus.OK, "BOARD-S002", "Board Read Success"),
    BOARD_SORTED_LIST_SUCCESS(HttpStatus.OK, "BOARD-S003", "Board Sorted List Success"),
    MY_BOARD_LIST_SUCCESS(HttpStatus.OK, "BOARD-S004", "My Board List Success"),
    MY_LIKE_BOARD_LIST_SUCCESS(HttpStatus.OK, "BOARD-S005", "My Like Board List Success"),
    BOARD_IMAGE_UPLOAD_SUCCESS(HttpStatus.OK, "BOARD_S006", "Board Image Upload Success"),
    BOARD_IMAGE_DELETE_SUCCESS(HttpStatus.OK, "BOARD_S007", "Board Image Delete Success"),

    BOARD_NOT_FOUND(HttpStatus.NOT_FOUND, "BOARD-F001", "Board Not Found"),
    SORT_TYPE_NOT_FOUND(HttpStatus.BAD_REQUEST, "BOARD-F002", "Sort Type Invalid"),
    BOARD_IMAGE_UPLOAD_FAIL(HttpStatus.BAD_REQUEST, "BOARD_F003", "Board Image Upload Fail"),

    /* 좋아요 - LIKE */
    LIKE_REGISTER_SUCCESS(HttpStatus.CREATED, "LIKE-S001", "Like Register Success"),
    LIKE_CANCEL_SUCCESS(HttpStatus.OK, "LIKE-S002", "Like Cancel Success"),
    GET_LIKE_STATUS_SUCCESS(HttpStatus.OK, "LIKE-S003", "Get Like Status Success"),

    LIKE_NOT_FOUND(HttpStatus.NOT_FOUND, "LIKE-F001", "Like Not Found"),

    /* 댓글 - COMMENTS */
    COMMENT_WRITE_SUCCESS(HttpStatus.CREATED,"COMMENT-S001","Comment Write Success"),

    /* 공통(시스템) - COMM */
    UNCAUGHT_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMM-F001", "Uncaught Error Occur"),
    DTO_INVALID(HttpStatus.BAD_REQUEST, "COMM-0002", "DTO Validate Fail");

    private HttpStatus status;
    private String code;
    private String message;

    public HttpStatus getStatus() {
        return status;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    ResponseType(final HttpStatus status, final String code, final String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    private void setDtoInvalidMessage(final String message) {
        this.message = message;
    }

    public static ResponseType dtoInvalid(final String message) {
        final ResponseType responseType = DTO_INVALID;
        responseType.setDtoInvalidMessage(message);
        return responseType;
    }

}