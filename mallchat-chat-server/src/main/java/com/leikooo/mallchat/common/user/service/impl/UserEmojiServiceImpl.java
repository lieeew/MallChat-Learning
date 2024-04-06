package com.leikooo.mallchat.common.user.service.impl;

import com.leikooo.mallchat.common.common.domain.vo.response.IdRespVO;
import com.leikooo.mallchat.common.common.utils.AssertUtil;
import com.leikooo.mallchat.common.user.dao.UserEmojiDao;
import com.leikooo.mallchat.common.user.domain.entity.UserEmoji;
import com.leikooo.mallchat.common.user.domain.vo.request.user.UserEmojiReq;
import com.leikooo.mallchat.common.user.domain.vo.response.user.UserEmojiResp;
import com.leikooo.mallchat.common.user.service.UserEmojiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.apache.ibatis.io.SerialFilterChecker.check;

/**
 * @author <a href="https://github.com/lieeew">leikooo</a>
 * @date 2024/4/5
 * @description
 */
@Service
public class UserEmojiServiceImpl implements UserEmojiService {
    @Resource
    private UserEmojiDao userEmojiDao;

    @Override
    public List<UserEmojiResp> list(Long uid) {
        List<UserEmoji> userEmojis = userEmojiDao.listById(uid);
        return userEmojis.stream().filter(Objects::nonNull)
                .map(userEmoji -> UserEmojiResp.builder().expressionUrl(userEmoji.getExpressionUrl()).id(userEmoji.getId()).build())
                .collect(Collectors.toList());
    }

    @Override
    public IdRespVO addEmoji(Long uid, UserEmojiReq req) {
        checkAddEmoji(uid);
        UserEmoji userEmoji = UserEmoji.builder().expressionUrl(req.getExpressionUrl()).uid(uid).build();
        userEmojiDao.save(userEmoji);
        return IdRespVO.id(userEmoji.getId());
    }

    private void checkAddEmoji(Long uid) {
        Integer count = userEmojiDao.countById(uid);
        AssertUtil.isFalse(count > 30, "添加的表情包超过 30 ~");
    }

    @Override
    public void deleteEmoji(Long uid, Long id) {
        UserEmoji userEmoji = userEmojiDao.getById(id);
        AssertUtil.equal(uid, userEmoji.getUid(), "小黑子不要盗刷接口~");
        userEmojiDao.removeById(id);
    }
}
