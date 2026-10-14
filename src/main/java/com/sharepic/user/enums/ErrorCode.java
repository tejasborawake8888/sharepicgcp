package com.sharepic.user.enums;

import lombok.Getter;

@Getter
public enum ErrorCode {
    VALIDATION_FAILED(101,"Validation Failed"),
    INTERNAL_ERROR(102,"Internal Server error"),
    USER_ALREADY_EXISTS(103,"User being created already exists");
    private final int errorCode;
    private final String msg;

    ErrorCode(int errorCode,String msg){
        this.errorCode = errorCode;
        this.msg = msg;
    }


}
