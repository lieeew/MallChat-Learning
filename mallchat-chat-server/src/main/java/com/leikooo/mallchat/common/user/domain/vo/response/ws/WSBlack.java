package com.leikooo.mallchat.common.user.domain.vo.response.ws;

import com.leikooo.mallchat.common.user.domain.enums.WSBaseResp;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Description:
 * @author  <a href="https://github.com/lieeew">leikooo</a>
 * Date: 2023-03-19
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WSBlack {
    private Long uid;
}
