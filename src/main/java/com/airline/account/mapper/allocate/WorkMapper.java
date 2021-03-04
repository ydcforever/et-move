package com.airline.account.mapper.allocate;

import com.airline.account.model.allocate.Work;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author ydc
 * @date 2021/2/24.
 */
@Repository
public interface WorkMapper {

    /**
     * 查询任务
     *
     * @param tableName
     * @return
     */
    List<Work> query(String tableName);

    /**
     * 查询未统计的待执行任务
     *
     * @param tableName
     * @return
     */
    List<Work> queryNoStatistic(String tableName);

    /**
     * 发现新文件
     * 和解析存在并行风险，增加顺序执行控制
     * 或者由解析完成这部分工作
     *
     * @param tableName
     * @return
     */
    List<String> findNewFile(@Param("tableName") String tableName);

    /**
     * 更新统计标志
     *
     * @param fileName
     */
    void updateHasStatistic(@Param("fileName") String fileName);
}
