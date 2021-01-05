package com.airline.account.utils;

import com.fate.piece.PagePiece;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author ydc
 * @date 2020/12/24.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class AllocateSource extends PagePiece {

    private String tableName;

    private String issueSql;

    private String configId;

    private String fileName;

    private String currentIssueDate;

    public AllocateSource(Integer pageSize, String tableName, String configId, String issueSql) {
        this.pageSize = pageSize;
        this.tableName = tableName;
        this.configId = configId;
        this.issueSql = issueSql;
    }
}
