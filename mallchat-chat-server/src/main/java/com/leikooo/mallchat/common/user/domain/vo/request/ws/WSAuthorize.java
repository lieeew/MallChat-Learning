package com.leikooo.mallchat.common.user.domain.vo.request.ws;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author <a href="https://github.com/zongzibinbin">leikooo</a>
 * Date: 2023-03-19
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WSAuthorize {
    private String token;
}
