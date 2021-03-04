package com.airline.account.model.allocate;

import com.fate.piece.PagePiece;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.util.Date;

/**
 * @author ydc
 * @date 2020/12/24.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class AllocateSource extends PagePiece {

    /**
     * 存储资源的表名
     */
    private String tableName;

    @Deprecated
    private String issueSql;

    @Deprecated
    private String moduleId;

    /**
     * 资源文件名
     */
    private String fileName;

    /**
     * 资源分割依据日期
     */
    private String issueDate;

    /**
     * 资源分割依据日期对应列名
     */
    private String dateColName;

    /**
     * 是否已经执行过
     */
    private String hasExecute;

    /**
     * 执行线程进度管理
     */
    private String threadId;

    private String start;

    private String end;

    private String status = "N";

    private String message;

    @Deprecated
    public AllocateSource(Integer pageSize, String tableName, String moduleId, String issueSql) {
        super(pageSize);
        this.tableName = tableName;
        this.moduleId = moduleId;
        this.issueSql = issueSql;
    }

    public AllocateSource(Integer pageSize, String tableName) {
        super(pageSize);
        this.tableName = tableName;
        this.dateColName = "ISSUE_DATE";
    }

    public void beginPiece(String threadId){
        this.start = currentDate();
        this.threadId = threadId;
    }

    public void finishPiece(String status){
        this.end = currentDate();
        this.status = status;
    }

    public void finishPiece(String status, String message){
        finishPiece(status);
        this.message = StringUtils.isBlank(message) ? "" : message.length() > 2000 ? message.substring(0, 1800) : message;
    }

    private String currentDate(){
        return DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss");
    }
}
