package com.leikooo.mallchat.transaction.dao;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.leikooo.mallchat.transaction.domain.entity.SecureInvokeRecord;
import com.leikooo.mallchat.transaction.mapper.SecureInvokeRecordMapper;
import com.leikooo.mallchat.transaction.service.SecureInvokeService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 本地消息表 服务实现类
 * </p>
 *
 * @author <a href="https://github.com/lieeew">leikooo</a>
 * @since 2024-03-13
 */
@Service
public class SecureInvokeRecordDao extends ServiceImpl<SecureInvokeRecordMapper, SecureInvokeRecord> {
    public List<SecureInvokeRecord> getWaitRetryRecords() {
        Date now = new Date();
        // 查2分钟前的失败数据。避免刚入库的数据被查出来
        DateTime afterTime = DateUtil.offsetMinute(now, (int) SecureInvokeService.RETRY_INTERVAL_MINUTES);
        return lambdaQuery()
                .eq(SecureInvokeRecord::getStatus, SecureInvokeRecord.STATUS_WAIT)
                .lt(SecureInvokeRecord::getNextRetryTime, afterTime)
                .lt(SecureInvokeRecord::getCreateTime, afterTime)
                .list();
    }
}
