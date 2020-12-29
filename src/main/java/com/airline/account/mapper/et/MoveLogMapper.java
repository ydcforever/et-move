package com.airline.account.mapper.et;

import com.airline.account.model.et.MoveLog;
import org.springframework.stereotype.Repository;

/**
 *
 * @author ydc
 * @date 2020/11/26
 */
@Repository
public interface MoveLogMapper {

     /**
      * 插入异常日志
      *
      * @param moveLog 日志
      */
     void insertLog(MoveLog moveLog);

}
