package com.airline.account.model.allocate;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ydc
 * @date 2021/2/24.
 */
@Data
@NoArgsConstructor
public class Work {
    /**
     * 存储资源的表名
     */
    private String tableName;

    /**
     * 资源文件名
     */
    private String fileName;

    /**
     * 是否已统计
     */
    private String hasStatistic;

    /**
     * 运行模式
     */
    private String runModel;

    /**
     * 执行状态
     */
    private String running;

    public Work(String tableName, String fileName) {
        this.tableName = tableName;
        this.fileName = fileName;
        this.hasStatistic = "N";
        this.runModel = "C";
        this.running = "N";
    }
}
