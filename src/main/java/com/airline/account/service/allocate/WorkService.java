package com.airline.account.service.allocate;

import com.airline.account.model.allocate.Work;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ydc
 * @date 2021/2/24.
 */
@Service
public interface WorkService {

    /**
     * 自动统计新文件
     *
     * @param tableName
     * @param dateCol
     */
    void preparedNewFile(String tableName, String dateCol);

    /**
     * 插入新的任务
     *
     * @param works
     */
    void insertWork(List<Work> works);

    /**
     * 资源分割执行后，判断该文件是否被完整正确处理
     *
     * @param tableName
     */
    void updateWorkRunning(String tableName);
}

