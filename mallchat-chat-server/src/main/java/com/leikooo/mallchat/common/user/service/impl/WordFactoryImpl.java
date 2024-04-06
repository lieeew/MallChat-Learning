package com.leikooo.mallchat.common.user.service.impl;

import com.leikooo.mallchat.common.sensitive.dao.SensitiveWordDao;
import com.leikooo.mallchat.common.sensitive.domain.SensitiveWord;
import com.leikooo.mallchat.common.user.service.WordFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author <a href="https://github.com/lieeew">leikooo</a>
 * @date 2024/4/6
 * @description
 */
@Service
public class WordFactoryImpl implements WordFactory {
    @Resource
    private SensitiveWordDao sensitiveWordDao;

    @Override
    public List<String> getWordList() {
        return sensitiveWordDao.list().stream()
                .map(SensitiveWord::getWord).collect(Collectors.toList());
    }
}
