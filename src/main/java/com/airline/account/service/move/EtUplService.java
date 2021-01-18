package com.airline.account.service.move;

import com.airline.account.model.et.EtUpl;
import org.springframework.stereotype.Service;

/**
 *
 * @author ydc
 * @date 2020/12/2
 */
@Service
public interface EtUplService {

    /**
     * 插入 ET UPL
     *
     * @param logGroup 日志组
     * @param etUpl upl/iwb
     */
    void insertEtUplWithUpdate(String logGroup, EtUpl etUpl);
}
