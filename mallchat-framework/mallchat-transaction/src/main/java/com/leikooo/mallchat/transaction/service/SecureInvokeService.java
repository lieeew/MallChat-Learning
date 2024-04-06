package com.leikooo.mallchat.transaction.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.leikooo.mallchat.transaction.dao.SecureInvokeRecordDao;
import com.leikooo.mallchat.transaction.domain.dto.SecureInvokeDTO;
import com.leikooo.mallchat.transaction.domain.entity.SecureInvokeRecord;
import com.leikooo.mallchat.utils.JsonUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

/**
 * @author <a href="https://github.com/lieeew">leikooo</a>
 * @date 2024/3/13
 * @description
 */
@Slf4j
@Service
@AllArgsConstructor
public class SecureInvokeService {
    @Resource
    private SecureInvokeRecordDao recordDao;

    private final Executor executor;

    public static final double RETRY_INTERVAL_MINUTES = 2D;

    // todo 正式上线之后再打开
//    @Scheduled(cron = "*/10 * * * * ?")
    public void retryUnsuccessfulRecord() {
        log.info("SecureInvokeService retryUnsuccessfulRecord thread name: {}", Thread.currentThread().getName());
        List<SecureInvokeRecord> waitRetryRecords = recordDao.getWaitRetryRecords();
        waitRetryRecords.forEach(this::doAsync);
    }

    public void invoke(SecureInvokeRecord record, boolean async) {
        boolean inTransaction = TransactionSynchronizationManager.isActualTransactionActive();
        // 非事务状态，直接执行，不做任何保证。
        if (!inTransaction) {
            return;
        }
        save(record);
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                if (async) {
                    doAsync(record);
                } else {
                    doInvoke(record);
                }
            }
        });
    }

    private void save(SecureInvokeRecord record) {
        recordDao.save(record);
    }

    private void doInvoke(SecureInvokeRecord record) {
        SecureInvokeDTO secureInvokeDTO = record.getSecureInvokeDTO();
        try {
            SecureInvokeHolder.setInTransaction();
            String className = secureInvokeDTO.getClassName();
            Object bean = SpringUtil.getBean(Class.forName(className));
            String methodName = secureInvokeDTO.getMethodName();
            List<Class<?>> paramType = getParamType(secureInvokeDTO.getParameterTypes());
            Object[] args = getArgs(secureInvokeDTO.getArgs());
            Method declaredMethod = Class.forName(className).getDeclaredMethod(methodName, paramType.toArray(new Class[]{}));
            declaredMethod.invoke(bean, args);
            // 成功删除记录
            recordDao.removeById(record.getId());
        } catch (Exception e) {
            // 保存异常信息，更新数据库
            log.error("SecureInvokeService doInvoke error", e);
            retryRecord(record, e);
        } finally {
            SecureInvokeHolder.removeInTransaction();
        }
    }

    private void retryRecord(SecureInvokeRecord record, Exception e) {
        SecureInvokeRecord secureInvokeRecord = new SecureInvokeRecord();
        BeanUtils.copyProperties(record, secureInvokeRecord);
        secureInvokeRecord.setFailReason(ExceptionUtils.getStackTrace(e));
        secureInvokeRecord.setNextRetryTime(getNextRetryTime(record.getRetryTimes()));
        if (secureInvokeRecord.getRetryTimes() >= secureInvokeRecord.getMaxRetryTimes()) {
            secureInvokeRecord.setStatus(SecureInvokeRecord.STATUS_FAIL);
        } else {
            secureInvokeRecord.setRetryTimes(record.getRetryTimes() + 1);
        }
        recordDao.updateById(secureInvokeRecord);
    }


    private Date getNextRetryTime(Integer retryTimes) {
        // 重试时间指数上升 2m 4m 8m 16m
        double waitMinutes = Math.pow(RETRY_INTERVAL_MINUTES, retryTimes);
        return DateUtil.offsetMinute(new Date(), (int) waitMinutes);
    }

    private Object[] getArgs(String args) {
        List<Object> list = JsonUtils.toList(args, Object.class);
        return list.toArray(new Object[]{});
    }

    private List<Class<?>> getParamType(String parameterTypes) {
        List<String> obj = JsonUtils.toList(parameterTypes, String.class);
        return obj.stream().map(o -> {
            try {
                return Class.forName(o);
            } catch (ClassNotFoundException e) {
                log.error("SecureInvokeService class not fund", e);
            }
            return null;
        }).collect(Collectors.toList());
    }

    private void doAsync(SecureInvokeRecord record) {
        executor.execute(() -> {
            log.info("SecureInvokeService doAsync method name: {}", Thread.currentThread().getName());
            doInvoke(record);
        });
    }
}
