package com.sharepic.user.enums;

import lombok.Getter;

@Getter
public enum ErrorCode {
    VALIDATION_FAILED(101,"Validation Failed"),
    INTERNAL_ERROR(102,"Internal Server error"),
    USER_ALREADY_EXISTS(103,"User being created already exists"),
    INVALID_CREDENTIALS(104,"Invalid username/email or password"),
    USER_NOT_FOUND(105,"User not found"),
    PASSWORD_NOT_MATCHED(106, "Provided password dont match with user's password"),
    ACCESS_DENIED(107,"Access denied as wrong user type for method call");
    private final int errorCode;
    private final String msg;

    ErrorCode(int errorCode,String msg){
        this.errorCode = errorCode;
        this.msg = msg;
    }


}
