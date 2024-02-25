package com.leikooo.mallchat.common.common.exception;

import cn.hutool.http.ContentType;
import cn.hutool.json.JSONUtil;
import com.leikooo.mallchat.common.common.domain.vo.response.ApiResult;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.io.Charsets;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author <a href="https://github.com/lieeew">leikooo</a>
 * @date 2024/2/16
 * @description
 */
@Getter
@AllArgsConstructor
public enum HttpErrorEnum {
    ACCESS_DEFINE(401, "登录失效请重新登录"),
    USER_LOGIN_ACCESS_DEFINE(402, "您已被封禁"),
    ;

    private final Integer code;

    private final String des;

    public void sendHttpError(HttpServletResponse response) throws IOException {
        response.setStatus(code);
        ApiResult<?> responseData = ApiResult.fail(code, des);
        response.setContentType(ContentType.JSON.toString(Charsets.UTF_8));
        response.getWriter().write(JSONUtil.toJsonStr(responseData));
    }
}
