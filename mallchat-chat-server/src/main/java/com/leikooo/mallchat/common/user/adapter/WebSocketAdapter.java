package com.leikooo.mallchat.common.user.adapter;

import com.leikooo.mallchat.common.user.domain.entity.User;
import com.leikooo.mallchat.common.user.domain.enums.WSBaseResp;
import com.leikooo.mallchat.common.user.domain.enums.WSRespTypeEnum;
import com.leikooo.mallchat.common.user.domain.vo.response.ws.WSLoginSuccess;
import com.leikooo.mallchat.common.user.domain.vo.response.ws.WSLoginUrl;
import com.leikooo.mallchat.common.user.domain.vo.response.ws.WSMessage;
import me.chanjar.weixin.mp.bean.result.WxMpQrCodeTicket;

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
        resp.setType(WSRespTypeEnum.LOGIN_SUCCESS.getType());
        resp.setData(wsLoginSuccess);
        return resp;
    }

    public static WSBaseResp<?> buildWaitAuthorizedResp() {
        WSBaseResp<WSMessage> resp = new WSBaseResp<>();
        resp.setType(WSRespTypeEnum.LOGIN_SCAN_SUCCESS.getType());
        return resp;
    }
}
