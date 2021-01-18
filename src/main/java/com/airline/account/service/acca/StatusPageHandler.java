package com.airline.account.service.acca;

import com.airline.account.model.et.Relation;
import com.airline.account.utils.AllocateSource;
import com.airline.account.utils.Constant;
import com.fate.piece.PageHandler;
import com.fate.pool.normal.NormalPool;

/**
 * @author ydc
 * @date 2021/1/8.
 */
public interface StatusPageHandler extends Constant {

    /**
     * 创建分页处理
     *
     * @param pool 状态池
     * @param allocateSource 资源分配
     * @return 分页处理
     */
    PageHandler createPageHandler(NormalPool<Relation> pool, AllocateSource allocateSource);


}
