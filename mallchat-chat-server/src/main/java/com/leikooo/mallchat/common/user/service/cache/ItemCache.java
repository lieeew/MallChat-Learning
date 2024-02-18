package com.leikooo.mallchat.common.user.service.cache;

import com.leikooo.mallchat.common.user.dao.ItemConfigDao;
import com.leikooo.mallchat.common.user.domain.entity.ItemConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author <a href="https://github.com/lieeew">leikooo</a>
 * @date 2024/2/18
 * @description
 */
@Slf4j
@Component
public class ItemCache {
    @Resource
    private ItemConfigDao itemConfigDao;

    /**
     * 这个返回的是一个 ItemConfig 的列表，而且这个列表不需要状态
     */
    @Cacheable(value = "item", key = "'itemByType' + #type")
    public List<ItemConfig> getByType(Integer type) {
        return itemConfigDao.listItemConfig(type);
    }

    @CacheEvict(value = "item", key = "'itemByType' + #type")
    public void evictCache(Integer type) {
        log.info("key is itemByType{} cache is evicted", type);
    }
}
