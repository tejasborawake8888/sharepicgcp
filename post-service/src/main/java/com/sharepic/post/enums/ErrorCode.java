package com.sharepic.post.enums;

import lombok.Getter;

@Getter
public enum ErrorCode {
    POST_NOT_FOUND(101,"Post with postid could not be found"),
    ILLEGAL_USER_FOR_POST_UPDATE(102,"Illegal User is trying to update/delete the post");
    private final int errorCode;
    private final String msg;

    ErrorCode(int errorCode,String msg){
        this.errorCode = errorCode;
        this.msg = msg;
    }
}
