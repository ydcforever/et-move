package com.airline.account.utils;

import com.airline.account.model.allocate.AllocateSource;
import com.fate.piece.PageHandler;

/**
 * @author ydc
 * @date 2021/2/23.
 */
public interface Handler {

    /**
     * 生成PageHandler
     *
     * @param allocateSource 资源
     * @return PageHandler
     */
    PageHandler generate(AllocateSource allocateSource);
}
