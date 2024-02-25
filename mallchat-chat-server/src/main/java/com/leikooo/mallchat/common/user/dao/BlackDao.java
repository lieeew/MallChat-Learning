package com.leikooo.mallchat.common.user.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.leikooo.mallchat.common.user.domain.entity.Black;
import com.leikooo.mallchat.common.user.mapper.BlackMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 黑名单 服务实现类
 * </p>
 *
 * @author <a href="https://github.com/lieeew">leikooo</a>
 * @since 2024-02-23
 */
@Service
public class BlackDao extends ServiceImpl<BlackMapper, Black> {

}
