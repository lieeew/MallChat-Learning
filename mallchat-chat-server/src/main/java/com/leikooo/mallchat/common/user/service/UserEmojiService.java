package com.leikooo.mallchat.common.user.service;

import com.leikooo.mallchat.common.common.domain.vo.response.ApiResult;
import com.leikooo.mallchat.common.common.domain.vo.response.IdRespVO;
import com.leikooo.mallchat.common.user.domain.vo.request.user.UserEmojiReq;
import com.leikooo.mallchat.common.user.domain.vo.response.user.UserEmojiResp;

import java.util.List;

/**
 * @author <a href="https://github.com/lieeew">leikooo</a>
 * @date 2024/4/5
 * @description userEmoji 接口
 */
public interface UserEmojiService {
    /**
     * 表情包列表
     */
    List<UserEmojiResp> list(Long uid);

    /**
     * 新增的 emoji
     *
     * @param uid  userId
     * @param req 表情包请求
     * @return EmojiId
     */
    IdRespVO addEmoji(Long uid, UserEmojiReq req);

    /**
     * 删除表情包
     *
     * @param uid 用户 uid
     * @param id  表情包 id
     */
    void deleteEmoji(Long uid, Long id);

}
