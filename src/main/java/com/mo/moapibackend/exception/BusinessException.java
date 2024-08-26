package com.mo.moapibackend.exception;

/**
 * 自定义异常类
 */

public class BusinessException extends RuntimeException{

    /**
     * 错误码
     */
    private int code;

    public int getCode() {
        return code;
    }

    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }
    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
    }
    public BusinessException(ErrorCode errorCode,String message) {
        super(message);
        this.code = errorCode.getCode();
    }
}
