package com.airline.account.service.allocate;

import com.airline.account.model.allocate.AllocateSource;
import org.springframework.stereotype.Service;

/**
 * @author ydc
 * @date 2021/2/24.
 */
@Service
public interface ThreadLogService {

    /**
     * 插入日志
     * @param allocateSource
     */
    void insertLog(AllocateSource allocateSource);

    /**
     * 更新日志
     * @param allocateSource
     */
    void updateLog(AllocateSource allocateSource);
}
