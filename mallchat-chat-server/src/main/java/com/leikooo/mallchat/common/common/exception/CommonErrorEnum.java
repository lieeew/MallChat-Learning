package com.leikooo.mallchat.common.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.lang.reflect.Member;

/**
 * @author <a href="https://github.com/lieeew">leikooo</a>
 * @date 2024/2/16
 * @description
 */
@Getter
@AllArgsConstructor
public enum CommonErrorEnum implements ErrorEnum{
    LOCK_LIMIT(-3, "请求太频繁，请稍后再试"),
    PARAM_INVALID(-2, "参数校验失败"),
    SYSTEM_ERROR(-1, "系统异常"),
    BUSINESS_ERROR(0, "业务异常"),
    FREQUENCY_LIMIT(-3, "请求太频繁了，请稍后再试哦~~"),
    ;

    private final Integer code;

    private final String des;

    @Override
    public Integer getErrorCode() {
        return code;
    }

    @Override
    public String getErrorMsg() {
        return des;
    }
}
