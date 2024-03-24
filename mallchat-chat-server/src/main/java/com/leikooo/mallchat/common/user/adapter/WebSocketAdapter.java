package com.leikooo.mallchat.common.user.adapter;

import com.leikooo.mallchat.common.chat.domain.entity.Message;
import com.leikooo.mallchat.common.chat.domain.vo.response.ChatMessageResp;
import com.leikooo.mallchat.common.common.domain.dto.PushMessageDTO;
import com.leikooo.mallchat.common.user.domain.entity.User;
import com.leikooo.mallchat.common.user.domain.enums.WSBaseResp;
import com.leikooo.mallchat.common.user.domain.enums.WSRespTypeEnum;
import com.leikooo.mallchat.common.user.domain.vo.response.ws.*;
import me.chanjar.weixin.mp.bean.result.WxMpQrCodeTicket;

import java.util.List;
import java.util.Objects;

import static com.leikooo.mallchat.common.user.domain.enums.WSRespTypeEnum.*;

/**
 * @author <a href="https://github.com/lieeew">leikooo</a>
 * @date 2024/2/10
 * @description
 */
public class WebSocketAdapter {
    public static WSBaseResp<?> buildLoginUrlResp(WxMpQrCodeTicket wxMpQrCodeTicket) {
        WSBaseResp<WSLoginUrl> resp = new WSBaseResp<>();
        resp.setType(WSRespTypeEnum.LOGIN_URL.getType());
        resp.setData(WSLoginUrl.builder().loginUrl(wxMpQrCodeTicket.getUrl()).build());
        return resp;
    }

    public static WSBaseResp<?> buildLoginSuccessResp(User user, String token) {
        WSLoginSuccess wsLoginSuccess = WSLoginSuccess.builder()
                .token(token)
                .avatar(user.getAvatar())
                .name(user.getName())
                .uid(user.getId())
                .build();
        WSBaseResp<WSLoginSuccess> resp = new WSBaseResp<>();
        resp.setType(LOGIN_SUCCESS.getType());
        resp.setData(wsLoginSuccess);
        return resp;
    }

    public static WSBaseResp<?> buildWaitAuthorizedResp() {
        WSBaseResp<WSMessage> resp = new WSBaseResp<>();
        resp.setType(LOGIN_SCAN_SUCCESS.getType());
        return resp;
    }

    public static WSBaseResp<?> buildAuthorizedResp(User user, String token) {
        return buildLoginSuccessResp(user, token);
    }

    public static WSBaseResp<?> buildInvalidToken() {
        WSBaseResp<Object> wsBaseResp = new WSBaseResp<>();
        wsBaseResp.setType(INVALIDATE_TOKEN.getType());
        return wsBaseResp;
    }

    public static WSBaseResp<?> buildBlackResp(Long id) {
        WSBlack wsBlack = WSBlack.builder().uid(id).build();
        return WSBaseResp.<WSBlack>builder()
                .type(WSRespTypeEnum.BLACK.getType())
                .data(wsBlack)
                .build();
    }

    /**
     * 发送给某一个 uid 的未阅读的消息
     *
     * @param unReadCount
     * @param targetId
     * @return
     */
    public static WSBaseResp<?> buildApplySend(int unReadCount, Long targetId) {
        WSFriendApply data = WSFriendApply.builder().uid(targetId).unreadCount(unReadCount).build();
        return WSBaseResp.<WSFriendApply>builder()
                .data(data)
                .type(APPLY.getType())
                .build();
    }

    public static WSBaseResp<?> buildNewMsgResp(ChatMessageResp chatMessageResp) {
        return WSBaseResp.<ChatMessageResp>builder()
                .type(MESSAGE.getType())
                .data(chatMessageResp)
                .build();
    }

    /**
     * 推送给 hot 群聊的所有人
     */
    public static PushMessageDTO buildPushAllMsg(ChatMessageResp chatMessageResp) {
        return new PushMessageDTO(chatMessageResp.getFromUser().getUid(), buildNewMsgResp(chatMessageResp));
    }

    public static PushMessageDTO buildPushMsg(ChatMessageResp chatMessageResp, List<Long> roomUsersId) {
        return new PushMessageDTO(roomUsersId, buildNewMsgResp(chatMessageResp));
    }

    public static WSBaseResp<?> buildRecallMessage(Message msg, User user) {
        return WSBaseResp.<String>builder()
                .type(RECALL.getType())
                .data(Objects.equals(msg.getFromUid(), user.getId()) ? "\"" + user.getName() + "\"撤回了一条消息" : "管理员撤回了\"" + user.getName() + "\"消息")
                .build();
    }
}
