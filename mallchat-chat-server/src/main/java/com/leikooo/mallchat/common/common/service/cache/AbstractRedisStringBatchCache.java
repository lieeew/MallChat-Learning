package com.leikooo.mallchat.common.common.service.cache;

import cn.hutool.core.util.ObjectUtil;
import com.leikooo.mallchat.common.common.utils.RedisUtils;
import javafx.util.Pair;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.ParameterizedType;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author <a href="https://github.com/lieeew">leikooo</a>
 * @date 2024/3/5
 * @description redis string 类型的批量缓存框架
 * 这个框架就像是 @Cacheable 一样让使用者只需要使用不需要太在意底层的实现
 */
public abstract class AbstractRedisStringBatchCache<IN, OUT> implements BatchCache<IN, OUT> {
    private Class<OUT> outClass;

    public AbstractRedisStringBatchCache() {
        // 通过反射获取 OUT 的真正属性
        ParameterizedType genericSuperclass = (ParameterizedType) this.getClass().getGenericSuperclass();
        this.outClass = (Class<OUT>) genericSuperclass.getActualTypeArguments()[1];
    }

    protected abstract String getKey(IN req);

    protected abstract long getExpireSeconds();

    /**
     * 加载数据
     */
    protected abstract Map<IN, OUT> load(List<IN> req);

    @Override
    public OUT get(IN req) {
        return getBatch(Collections.singletonList(req)).get(req);
    }

    @Override
    public Map<IN, OUT> getBatch(List<IN> req) {
        // 防御性编程
        if (CollectionUtils.isEmpty(req)) {
            return new HashMap<>();
        }
        req = req.stream().distinct().collect(Collectors.toList());
        List<String> keyList = req.stream().map(this::getKey).collect(Collectors.toList());
        List<OUT> valueList = RedisUtils.mget(keyList, outClass);
        List<IN> loadReqs = new ArrayList<>();
        for (int i = 0; i < valueList.size(); i++) {
            // 如果没有数据，就需要加载
            if (ObjectUtil.isNull(valueList.get(i))) {
                loadReqs.add(req.get(i));
            }
        }
        // 需要使用 HashMap 因为如果不使用的有点麻烦
        // 后面组装 result 的时候我们是按照 req 的顺序去取值,但是如果 valueList 没有取到数据, 我们就需要通过 IN 来获取了
        HashMap<IN, OUT> load = new HashMap<>();
        if (!CollectionUtils.isEmpty(loadReqs)) {
            // 重新加载
            load = (HashMap<IN, OUT>) load(loadReqs);
            Map<String, OUT> collect = load.entrySet().stream().map(entry -> new Pair<>(getKey(entry.getKey()), entry.getValue())).collect(Collectors.toMap(Pair::getKey, Pair::getValue));
            RedisUtils.mset(collect, getExpireSeconds());
        }
        // 输出最后的结果
        Map<IN, OUT> result = new HashMap<>();
        for (int i = 0; i < req.size(); i++) {
            IN in = req.get(i);
            OUT out = Optional.ofNullable(valueList.get(i)).orElse(load.get(in));
            result.put(in, out);
        }
        return result;
    }


    @Override
    public void delete(IN req) {
        deleteBatch(Collections.singletonList(req));
    }

    @Override
    public void deleteBatch(List<IN> req) {
        List<String> uidList = req.stream().map(this::getKey).collect(Collectors.toList());
        RedisUtils.del(uidList);
    }
}
