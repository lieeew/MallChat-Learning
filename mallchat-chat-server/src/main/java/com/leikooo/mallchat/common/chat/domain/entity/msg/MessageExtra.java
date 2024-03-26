package com.leikooo.mallchat.common.chat.domain.entity.msg;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.leikooo.mallchat.common.common.utils.discover.UrlInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Description: 消息扩展属性
 * @author : <a href="https://github.com/zongzibinbin">abin</a>
 * Date: 2023-05-28
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MessageExtra implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 回复的消息内容
     */
    private Long replyMsgId;

    /**
     * 与回复消息的间隔条数
     */
    private Integer gapCount;

    /**
     * url跳转链接
     */
    private Map<String, UrlInfo> urlContentMap;

    /**
     * 消息撤回详情
     */
    private MsgRecall recall;

    /**
     * 艾特的 uid
     */
    private List<Long> atUidList;

    /**
     * 文件消息
     */
    private FileMsgDTO fileMsg;

    /**
     * 图片消息
     */
    private ImgMsgDTO imgMsgDTO;

    /**
     * 语音消息
     */
    private SoundMsgDTO soundMsgDTO;

    /**
     * 文件消息
     */
    private VideoMsgDTO videoMsgDTO;

    /**
     * 表情图片信息
     */
    private EmojisMsgDTO emojisMsgDTO;
}
