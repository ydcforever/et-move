package com.airline.account.service.move;

import com.airline.account.utils.AllocateSource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 * @author ydc
 * @date 2020/12/19
 */
@Service
public interface LoadSourceService {

    /**
     * 获取要处理的资源文件
     *
     * @param module 配置id
     * @return list of file name
     */
    List<String> getSource(String module);

    /**
     * 更新处理文件的结果
     *
     * @param module 配置id
     * @param source 文件名
     * @param status 状态
     */
    void updateStatus(String module, String source, String status);

    /**
     * 获取文件中涉及的日期
     *
     * @param allocateSource 资源分配
     * @return list of issue date
     */
    List<String> getIssueDates(AllocateSource allocateSource);
}
