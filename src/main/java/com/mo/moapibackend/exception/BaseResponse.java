package com.mo.moapibackend.exception;

import lombok.Data;

import java.io.Serializable;

/**
 * 通用返回类
 * @author lengmo
 */
@Data
public class BaseResponse<T> implements Serializable {


    private static final long serialVersionUID = -7048093207224758446L;

    private int code;

    private T data;

    private String message;

    public BaseResponse(int code, T data, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
    }
    public BaseResponse(int code, T data) {
        this.code = code;
        this.data = data;
    }
    public BaseResponse(ErrorCode errorCode) {
        this(errorCode.getCode(),null, errorCode.getMessage());
    }
}
