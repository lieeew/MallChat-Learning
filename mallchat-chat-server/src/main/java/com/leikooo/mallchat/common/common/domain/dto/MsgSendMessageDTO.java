package com.leikooo.mallchat.common.common.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * Description:
 * @author <a href="https://github.com/lieeew">leikooo</a>
 * Date: 2023-08-12
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MsgSendMessageDTO implements Serializable {
    private Long msgId;
}
