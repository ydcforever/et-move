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

    private String airlineCode;

    private String firstTicketNo;

    private String excp;

    private String createTime;

    public MoveLog(String airlineCode, String firstTicketNo, String excp) {
        this.airlineCode = airlineCode;
        this.firstTicketNo = firstTicketNo;
        this.excp = StringUtils.isBlank(excp) ? "" : excp.length() > 2000 ? excp.trim().substring(0, 1500) : excp;
        this.createTime = DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss");
    }
}
