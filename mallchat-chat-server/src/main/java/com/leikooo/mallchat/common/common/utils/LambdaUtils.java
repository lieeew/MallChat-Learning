package com.leikooo.mallchat.common.common.utils;

import cn.hutool.core.map.WeakConcurrentMap;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.support.ColumnCache;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import lombok.SneakyThrows;
import org.apache.ibatis.reflection.property.PropertyNamer;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author <a href="https://github.com/zongzibinbin">abin</a>
 * @description LambdaUtils
 */
public class LambdaUtils {
    /**
     * SerializedLambda 反序列化缓存
     */
    private static final WeakConcurrentMap<String, SerializedLambda> cache = new WeakConcurrentMap<>();

    @SneakyThrows
    public static <T> Class<?> getReturnType(SFunction<T, ?> func) {
        // todo 这里可以深入了解一下，作为亮点的亮点
        com.baomidou.mybatisplus.core.toolkit.support.SerializedLambda lambda = com.baomidou.mybatisplus.core.toolkit.LambdaUtils.resolve(func);
        Class<?> aClass = lambda.getInstantiatedType();
        String fieldName = PropertyNamer.methodToProperty(lambda.getImplMethodName());
        Field field = aClass.getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.getType();
    }

    /**
     * 解析lambda表达式,加了缓存。
     * 该缓存可能会在任意不定的时间被清除。
     *
     * <p>
     * 通过反射调用实现序列化接口函数对象的writeReplace方法，从而拿到{@link SerializedLambda}<br>
     * 该对象中包含了lambda表达式的所有信息。
     * </p>
     *
     * @param func 需要解析的 lambda 对象
     * @return 返回解析后的结果
     */
    private static SerializedLambda _resolve(Serializable func) {
        return cache.computeIfAbsent(func.getClass().getName(), (key)
                -> ReflectUtil.invoke(func, "writeReplace"));
    }

}
