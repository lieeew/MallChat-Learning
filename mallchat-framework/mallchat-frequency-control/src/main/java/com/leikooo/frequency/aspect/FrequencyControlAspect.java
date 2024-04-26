package com.leikooo.frequency.aspect;


import cn.hutool.core.util.StrUtil;
import com.leikooo.frequency.adaptor.FrequencyAdaptor;
import com.leikooo.frequency.annotation.FrequencyControl;
import com.leikooo.frequency.domain.dto.FrequencyControlDTO;
import com.leikooo.frequency.domain.dto.SlidingWindowDTO;
import com.leikooo.frequency.domain.dto.TokenBucketDTO;
import com.leikooo.frequency.service.FrequencyControlUtil;
import com.leikooo.frequency.util.RequestHolder;
import com.leikooo.mallchat.common.FrequencyControlConstant;
import com.leikooo.mallchat.utils.SpElUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 频控实现
 *
 * @author liang
 */
@Slf4j
@Aspect
@Component
public class FrequencyControlAspect {
    @Around("@annotation(com.leikooo.frequency.annotation.FrequencyControl)||@annotation(com.leikooo.frequency.annotation.FrequencyControlContainer)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        FrequencyControl[] annotationsByType = method.getAnnotationsByType(FrequencyControl.class);
        Map<String, FrequencyControl> keyMap = new HashMap<>();
        String strategy = FrequencyControlConstant.TOTAL_COUNT_WITH_IN_FIX_TIME;
        for (int i = 0; i < annotationsByType.length; i++) {
            // 获取频控注解
            FrequencyControl frequencyControl = annotationsByType[i];
            String prefix = StrUtil.isBlank(frequencyControl.prefixKey()) ? /* 默认方法限定名 + 注解排名（可能多个）*/method.toGenericString() + ":index:" + i : frequencyControl.prefixKey();
            String key = getKey(joinPoint, method, frequencyControl);
            keyMap.put(prefix + ":" + key, frequencyControl);
            strategy = frequencyControl.strategy();
        }
        return frequencyControlAndExecute(strategy, keyMap, joinPoint);
    }

    private String getKey(ProceedingJoinPoint joinPoint, Method method, FrequencyControl frequencyControl) {
        String key = "";
        switch (frequencyControl.target()) {
            case EL:
                key = SpElUtils.parseSpEl(method, joinPoint.getArgs(), frequencyControl.spEl());
                break;
            case IP:
                key = RequestHolder.get().getIp();
                break;
            case UID:
                key = RequestHolder.get().getUid().toString();
        }
        return key;
    }

    private Object frequencyControlAndExecute(String strategy, Map<String, ? extends FrequencyControl> keyMap, ProceedingJoinPoint joinPoint) throws Throwable {
        // 将注解的参数转换为编程式调用需要的参数
        if (FrequencyControlConstant.TOTAL_COUNT_WITH_IN_FIX_TIME.equals(strategy)) {
            // 调用编程式注解 固定窗口
            List<FrequencyControlDTO> frequencyControlDto = keyMap.entrySet().stream().map(entrySet -> FrequencyAdaptor.buildFixedWindowDTO(entrySet.getKey(), entrySet.getValue())).collect(Collectors.toList());
            return FrequencyControlUtil.executeWithFrequencyControlList(strategy, frequencyControlDto, joinPoint::proceed);
        } else if (FrequencyControlConstant.TOKEN_BUCKET.equals(strategy)) {
            // 调用编程式注解 令牌桶
            List<TokenBucketDTO> frequencyControlDto = keyMap.entrySet().stream().map(entrySet -> FrequencyAdaptor.buildTokenBucketDTO(entrySet.getKey(), entrySet.getValue())).collect(Collectors.toList());
            return FrequencyControlUtil.executeWithFrequencyControlList(strategy, frequencyControlDto, joinPoint::proceed);
        } else if (FrequencyControlConstant.SLIDING_WINDOW.equals(strategy)) {
            // 调用编程式注解 滑动窗口
            List<SlidingWindowDTO> frequencyControlDto = keyMap.entrySet().stream().map(entrySet -> FrequencyAdaptor.buildSlidingWindowFrequencyControlDTO(entrySet.getKey(), entrySet.getValue())).collect(Collectors.toList());
            return FrequencyControlUtil.executeWithFrequencyControlList(strategy, frequencyControlDto, joinPoint::proceed);
        }
        return null;
    }
}
