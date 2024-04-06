package com.leikooo.mallchat.common.sensitive;

import com.leikooo.mallchat.common.sensitive.dao.SensitiveWordDao;
import com.leikooo.mallchat.common.sensitive.domain.SensitiveWord;
import com.leikooo.mallchat.common.user.service.WordFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author <a href="https://github.com/lieeew">leikooo</a>
 */
@Component
public class MyWordFactory implements WordFactory {
    @Resource
    private SensitiveWordDao sensitiveWordDao;

    @Override
    public List<String> getWordList() {
        return sensitiveWordDao.list()
                .stream()
                .map(SensitiveWord::getWord)
                .collect(Collectors.toList());
    }
}
