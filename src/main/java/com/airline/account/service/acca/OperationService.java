package com.airline.account.service.acca;

/**
 * @author ydc
 * @date 2021/2/4.
 */
public interface OperationService<T> {

    /**
     * 更新插入标志
     * @param t 处理对象
     * @param tableName 表
     */
    default void completeInsert(T t, String tableName) {

    };

    /**
     * 更新更新标志
     * @param t 处理对象
     * @param tableName 表
     */
    default void completeUpdate(T t, String tableName){

    };
}
