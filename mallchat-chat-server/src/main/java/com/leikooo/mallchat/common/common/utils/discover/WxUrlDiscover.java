package com.leikooo.mallchat.common.common.utils.discover;

import io.micrometer.core.lang.Nullable;
import org.jsoup.nodes.Document;

/**
 * @author <a href="https://github.com/lieeew">leikooo</a>
 * @date 2024/4/4
 * @description
 */
public class WxUrlDiscover extends AbstractUrlDiscover {
    @Nullable
    @Override
    public String getTitle(Document document) {
        return document.getElementsByAttributeValue("property", "og:title").attr("content");
    }

    @Nullable
    @Override
    public String getDescription(Document document) {
        return document.getElementsByAttributeValue("property", "og:description").attr("content");
    }

    @Nullable
    @Override
    public String getImage(String url, Document document) {
        String href = document.getElementsByAttributeValue("property", "og:image").attr("content");
        return isConnect(href) ? href : null;
    }
}
