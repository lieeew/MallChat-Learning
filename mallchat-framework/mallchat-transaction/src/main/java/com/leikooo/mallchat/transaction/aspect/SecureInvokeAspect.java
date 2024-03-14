package com.leikooo.mallchat.transaction.aspect;

import com.leikooo.mallchat.transaction.annotation.SecureInvoke;
import com.leikooo.mallchat.transaction.dao.SecureInvokeRecordDao;
import com.leikooo.mallchat.transaction.domain.dto.SecureInvokeDTO;
import com.leikooo.mallchat.transaction.domain.entity.SecureInvokeRecord;
import com.leikooo.mallchat.transaction.service.SecureInvokeHolder;
import com.leikooo.mallchat.transaction.service.SecureInvokeService;
import com.leikooo.mallchat.utils.JsonUtils;
import io.swagger.models.auth.In;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.junit.jupiter.api.Order;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Date;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author <a href="https://github.com/lieeew">leikooo</a>
 * @date 2024/3/13
 * @description
 */
@Aspect
@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 1)
public class SecureInvokeAspect {
    @Resource
    private SecureInvokeService secureInvokeService;

    @Around("@annotation(secureInvoke)")
    public Object around(ProceedingJoinPoint joinPoint, SecureInvoke secureInvoke) throws Throwable {
        boolean async = secureInvoke.async();
        boolean inTransaction = TransactionSynchronizationManager.isActualTransactionActive();
        if (SecureInvokeHolder.getIsInTransaction() || !inTransaction) {
            return joinPoint.proceed();
        }
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        // 保存消息持久化到数据库
        SecureInvokeRecord record = getSecureInvokeRecord(joinPoint, secureInvoke, method);
        secureInvokeService.invoke(record, async);
        return null;
    }

    private static SecureInvokeRecord getSecureInvokeRecord(ProceedingJoinPoint joinPoint, SecureInvoke secureInvoke, Method method) {
        SecureInvokeDTO secureInvokeDTO = SecureInvokeDTO.builder()
                .args(JsonUtils.toStr(joinPoint.getArgs()))
                .className(method.getDeclaringClass().getName())
                .parameterTypes(JsonUtils.toStr(Stream.of(method.getParameterTypes()).map(Class::getName).collect(Collectors.toList())))
                .methodName(method.getName())
                .build();
        return SecureInvokeRecord.builder()
                .secureInvokeDTO(secureInvokeDTO)
                .createTime(new Date())
                .maxRetryTimes(secureInvoke.maxRetryTimes())
                .build();
    }

}
