package com.leikooo.mallchat.common.common.exception;

import com.leikooo.mallchat.common.common.domain.vo.response.ApiResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author <a href="https://github.com/lieeew">leikooo</a>
 * @date 2024/2/16
 * @description
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResult<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("参数校验失败 ", e);
        StringBuilder sb = new StringBuilder();
        e.getBindingResult().getFieldErrors().forEach(error -> sb.append(error.getField()).append(" ").append(error.getDefaultMessage()).append(", "));
        return ApiResult.fail(CommonErrorEnum.PARAM_INVALID.getCode(), sb.substring(0, sb.length() - 2));
    }

    @ExceptionHandler(BusinessException.class)
    public ApiResult<?> handleBusinessException(BusinessException e) {
        log.error("BUSINESS ERROR The reason is {}", e.getMessage(), e);
        return ApiResult.fail(e.getCode(), e.getDes());
    }

    /**
     * 兜底的异常处理
     */
    @ExceptionHandler(Throwable.class)
    public ApiResult<?> handleException(Exception e) {
        log.error("SYSTEM ERROR The reason is {}", e.getMessage(), e);
        return ApiResult.fail(CommonErrorEnum.SYSTEM_ERROR);
    }
}
