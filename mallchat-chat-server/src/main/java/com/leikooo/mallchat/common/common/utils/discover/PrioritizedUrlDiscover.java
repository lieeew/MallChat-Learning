package com.leikooo.mallchat.common.common.utils.discover;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="https://github.com/lieeew">leikooo</a>
 * @date 2024/4/4
 * @description
 */
public class PrioritizedUrlDiscover extends AbstractUrlDiscover {
    private final List<AbstractUrlDiscover> urlDiscovers = new ArrayList<>(2);

    public PrioritizedUrlDiscover() {
        urlDiscovers.add(new WxUrlDiscover());
        urlDiscovers.add(new CommonUrlDiscover());
    }


    @Override
    public String getTitle(Document document) {
        for (UrlDiscover urlDiscover : urlDiscovers) {
            String title = urlDiscover.getTitle(document);
            if (StringUtils.isNotBlank(title)) {
                return title;
            }
        }
        return null;
    }

    @Override
    public String getDescription(Document document) {
        for (UrlDiscover urlDiscover : urlDiscovers) {
            String urlDescription = urlDiscover.getDescription(document);
            if (StringUtils.isNotBlank(urlDescription)) {
                return urlDescription;
            }
        }
        return null;
    }

    @Override
    public String getImage(String url, Document document) {
        for (UrlDiscover urlDiscover : urlDiscovers) {
            String image = urlDiscover.getImage(url, document);
            if (StringUtils.isNotBlank(image)) {
                return image;
            }
        }
        return null;
    }
}
