package com.airline.account.model.et;

import lombok.Data;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.util.Date;

/**
 *
 * @author ydc
 * @date 2020/11/26
 */
@Data
public class MoveLog {

    private String executor;

    private String ticketNo;

    private String excp;

    private String createTime;

    public MoveLog(String executor, String ticketNo, String excp) {
        this.executor = executor;
        this.ticketNo = ticketNo;
        this.excp = StringUtils.isBlank(excp) ? "" : excp.length() > 2000 ? excp.substring(0, 1500) : excp;
        this.createTime = DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss");
    }
}
