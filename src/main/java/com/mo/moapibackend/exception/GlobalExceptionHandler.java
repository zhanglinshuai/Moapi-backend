package com.mo.moapibackend.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler  {
    @ExceptionHandler(BusinessException.class)
    public BaseResponse<?> handleBusinessException( BusinessException e) {
        log.error("BusinessException",e);
        return ResultUtils.failed(e.getCode(),e.getMessage());
    }
    @ExceptionHandler(RuntimeException.class)
    public BaseResponse<?> handleRuntimeException(RuntimeException e) {
        log.error("RuntimeException",e);
        return ResultUtils.failed(ErrorCode.SYSTEM_ERROR,"系统内部错误");
    }
}
