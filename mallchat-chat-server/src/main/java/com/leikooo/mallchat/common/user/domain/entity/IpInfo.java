package com.leikooo.mallchat.common.user.domain.entity;

import cn.hutool.core.util.ObjectUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;

/**
 * <p>
 * 用户ip信息
 * </p>
 *
 * @author <a href="https://github.com/lieeew">leikooo</a>
 * @since 2023-03-19
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IpInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 注册时的ip
     */
    private String createIp;

    /**
     * 注册时的ip详情
     */
    private IpDetail createIpDetail;

    /**
     * 最新登录的ip
     */
    private String updateIp;

    /**
     * 最新登录的ip详情
     */
    private IpDetail updateIpDetail;

    public void refreshIp(String ip) {
        if (StringUtils.isEmpty(ip)) {
            return;
        }
        if (StringUtils.isEmpty(createIp)) {
            // 第一次创建 ip
            this.createIp= ip;
        }
        this.updateIp = ip;
    }

    public String isNeedRefresh() {
        boolean notNeed = Optional.ofNullable(updateIpDetail)
                .map(IpDetail::getIp)
                .filter(ip -> ObjectUtil.equal(ip, updateIp))
                .isPresent();
        return notNeed ? null : updateIp;
    }

    public void refreshIpDetail(IpDetail ipDetail) {
        if ((ObjectUtil.equal(ipDetail.getIp(), createIp))) {
            createIpDetail = ipDetail;
        }
        if (ObjectUtil.equal(ipDetail.getIp(), updateIp)) {
            updateIpDetail = ipDetail;
        }
    }
}