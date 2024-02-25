package com.leikooo.mallchat.common.user.service.impl;

import cn.hutool.core.thread.NamedThreadFactory;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpUtil;
import com.leikooo.mallchat.utils.JsonUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.leikooo.mallchat.common.common.domain.vo.response.ApiResult;
import com.leikooo.mallchat.common.user.dao.UserDao;
import com.leikooo.mallchat.common.user.domain.entity.IpDetail;
import com.leikooo.mallchat.common.user.domain.entity.IpInfo;
import com.leikooo.mallchat.common.user.domain.entity.User;
import com.leikooo.mallchat.common.user.service.IpService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 通过继承 DisposableBean 实现优雅停机
 *
 * @author <a href="https://github.com/lieeew">leikooo</a>
 * @date 2024/2/22
 * @description
 */
@Service
@Slf4j
public class IpServiceImpl implements IpService, DisposableBean {
    public static final String GET_IP_INFO_REQUEST = "https://ip.taobao.com/outGetIpInfo?ip=%s&accessKey=alibaba-inc";

    /**
     * 线程池 增加前缀这样后续出了问题容易排查
     */
    private static final ExecutorService EXECUTOR = new ThreadPoolExecutor(1,
            1,
            0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>(500),
            new NamedThreadFactory("refresh-ipDetail", false));

    @Resource
    private UserDao userDao;

    @Override
    public void refreshIpDetailAsync(Long uid) {
        EXECUTOR.execute(() -> {
            User user = userDao.getById(uid);
            IpInfo ipInfo = user.getIpInfo();
            String ip = ipInfo.isNeedRefresh();
            IpDetail ipDetail = getIpDetailInfoThreeTime(ip);
            if (ObjectUtil.isNotNull(ipDetail)) {
                ipInfo.refreshIpDetail(ipDetail);
                User updateUser = User.builder()
                        .id(user.getId())
                        .ipInfo(ipInfo)
                        .build();
                userDao.updateById(updateUser);
            }
        });
    }

    private IpDetail getIpDetailInfoThreeTime(String ip) {
        if (StringUtils.isEmpty(ip)) {
            return null;
        }
        for (int i = 0; i < 3; i++) {
            IpDetail ipDetailInfo = getIpDetailInfo(ip);
            if (ObjectUtil.isNotNull(ipDetailInfo)) {
                return ipDetailInfo;
            }
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                log.error("IpServiceImpl#getIpDetailInfoThreeTime error", e);
            }
        }
        return null;
    }

    private IpDetail getIpDetailInfo(String ip) {
        try {
            ApiResult<IpDetail> result = JsonUtils.toObj(HttpUtil.get(String.format(GET_IP_INFO_REQUEST, ip)), new TypeReference<ApiResult<IpDetail>>() {
            });
            return result.getData();
        } catch (Exception e) {
            log.info("IpServiceImpl#getIpDetailInfo error", e);
        }
        return null;
    }

    @Override
    public void destroy() throws Exception {
        EXECUTOR.shutdown();
        // 等待 30s 如果没有完成就没有完成
        if (!EXECUTOR.awaitTermination(30, TimeUnit.SECONDS)) {
            if (log.isErrorEnabled()) {
                log.error("Timed out while waiting for executor [{}] to terminate", EXECUTOR);
            }
        }
    }
}
