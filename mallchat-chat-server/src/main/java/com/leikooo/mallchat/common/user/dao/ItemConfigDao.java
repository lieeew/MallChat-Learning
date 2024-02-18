package com.leikooo.mallchat.common.user.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.leikooo.mallchat.common.user.domain.entity.ItemConfig;
import com.leikooo.mallchat.common.user.mapper.ItemConfigMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 功能物品配置表 服务实现类
 * </p>
 *
 * @author <a href="https://github.com/lieeew">leikooo</a>
 * @since 2024-02-14
 */
@Service
public class ItemConfigDao extends ServiceImpl<ItemConfigMapper, ItemConfig>  {
    public List<ItemConfig> listItemConfig(Integer type) {
        return lambdaQuery()
                .eq(ItemConfig::getType, type)
                .list();
    }
}
