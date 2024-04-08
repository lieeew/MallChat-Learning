package com.leikooo.mallchat.common.frequency.exception;

import com.leikooo.mallchat.common.common.exception.ErrorEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 自定义限流异常
 * @author liang
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class FrequencyControlException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     *  错误码
     */
    protected Integer errorCode;

    /**
     *  错误信息
     */
    protected String errorMsg;

    public FrequencyControlException() {
        super();
    }

    public FrequencyControlException(String errorMsg) {
        super(errorMsg);
        this.errorMsg = errorMsg;
    }

    public FrequencyControlException(ErrorEnum error) {
        super(error.getErrorMsg());
        this.errorCode = error.getErrorCode();
        this.errorMsg = error.getErrorMsg();
    }
}
