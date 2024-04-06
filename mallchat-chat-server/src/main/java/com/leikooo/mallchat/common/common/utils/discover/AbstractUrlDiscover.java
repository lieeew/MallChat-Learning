package com.leikooo.mallchat.common.common.utils.discover;

import cn.hutool.core.lang.Pair;
import cn.hutool.core.thread.NamedThreadFactory;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import com.leikooo.mallchat.common.common.exception.BusinessException;
import com.leikooo.mallchat.common.common.exception.CommonErrorEnum;
import com.leikooo.mallchat.common.common.utils.FutureUtils;
import com.leikooo.mallchat.common.common.utils.discover.domain.UrlInfo;
import io.micrometer.core.lang.Nullable;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author <a href="https://github.com/lieeew">leikooo</a>
 * @date 2024/4/3
 * @description 责任链模式
 */
@Slf4j
public abstract class AbstractUrlDiscover implements UrlDiscover {

    private static final Pattern PATTERN = Pattern.compile("((http|https)://)?(www.)?([\\w_-]+(?:(?:\\.[\\w_-]+)+))([\\w.,@?^=%&:/~+#-]*[\\w@?^=%&/~+#-])?");

    @Nullable
    @Override
    public Map<String, UrlInfo> getUrlContent(String content) {
        if (StrUtil.isBlank(content)) {
            return new HashMap<>();
        }
        List<String> matchList = ReUtil.findAll(PATTERN, content, 0);
        return parallelExecuteWithUrls(matchList);
    }

    private Map<String, UrlInfo> parallelExecuteWithUrls(List<String> matchList) {
        Executor executor = Executors.newFixedThreadPool(Math.min(matchList.size(), 10), new NamedThreadFactory("url-content", false)); // 控制并行度
        List<CompletableFuture<Pair<String, UrlInfo>>> futures = new ArrayList<>(matchList.size());
        for (String match : matchList) {
            CompletableFuture<Pair<String, UrlInfo>> future = CompletableFuture
                    .supplyAsync(() -> getContent(match), executor) // 在自定义的Executor中执行任务
                    .thenApply(urlInfo -> Objects.isNull(urlInfo) ? null : Pair.of(match, urlInfo))
                    .exceptionally((e) -> {
                        log.error("AbstractUrlDiscover#parallelExecuteWithUrls error" + ExceptionUtils.getStackTrace(e));
                        throw new BusinessException(CommonErrorEnum.SYSTEM_ERROR);
                    });
            futures.add(future);
        }
        // 并行执行
        CompletableFuture<List<Pair<String, UrlInfo>>> future = FutureUtils.sequenceNonNull(futures);
        return future.join().stream().collect(Collectors.toMap(Pair::getKey, Pair::getValue));
    }

    @Override
    public UrlInfo getContent(String url) {
        Document urlDocument = getUrlDocument(assemble(url));
        if (Objects.isNull(urlDocument)) {
            return new UrlInfo();
        }
        return UrlInfo.builder()
                .description(getDescription(urlDocument))
                .image(getImage(assemble(url), urlDocument))
                .title(getTitle(urlDocument))
                .build();
    }

    protected Document getUrlDocument(String matchUrl) {
        try {
            Connection connect = Jsoup.connect(matchUrl);
            connect.timeout(2000);
            return connect.get();
        } catch (Exception e) {
            log.error("find error:url:{}", matchUrl, e);
        }
        return null;
    }

    private String assemble(String url) {

        if (!StrUtil.startWith(url, "http")) {
            return "http://" + url;
        }

        return url;
    }

    /**
     * 判断链接是否有效
     * 输入链接
     * 返回true或者false
     */
    public static boolean isConnect(String href) {
        // 请求地址
        URL url;
        // 请求状态码
        int state;
        // 下载链接类型
        String fileType;
        try {
            url = new URL(href);
            HttpURLConnection httpUrlConnection = (HttpURLConnection) url.openConnection();
            state = httpUrlConnection.getResponseCode();
            fileType = httpUrlConnection.getHeaderField("Content-Disposition");
            //如果成功200，缓存304，移动302都算有效链接，并且不是下载链接
            if ((state == 200 || state == 302 || state == 304) && fileType == null) {
                return true;
            }
            httpUrlConnection.disconnect();
        } catch (Exception e) {
            return false;
        }
        return false;
    }
}
