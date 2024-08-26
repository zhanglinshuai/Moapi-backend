package com.mo.moapibackend.exception;

/**
 * 通用返回类对象
 * @author lengmo
 */
public class ResultUtils {
    /**
     * 成功
     * @param data
     * @return
     * @param <T>
     */
    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>(0,data,"ok");
    }

    /**
     * 失败
     * @param code
     * @param message
     * @return
     * @param <T>
     */
    public static <T> BaseResponse<T> failed(int code,String message) {
        return new BaseResponse<>(code,null,message);
    }

    /**
     * 失败
     * @param errorCode
     * @return
     * @param <T>
     */
    public static <T> BaseResponse<T> failed(ErrorCode errorCode) {
        return new BaseResponse<>(errorCode.getCode(),null, errorCode.getMessage());
    }

    /**
     * 失败
     * @param errorCode
     * @param message
     * @return
     * @param <T>
     */
    public static <T> BaseResponse<T> failed(ErrorCode errorCode,String message) {
        return new BaseResponse<>(errorCode.getCode(),null, message);
    }
}
