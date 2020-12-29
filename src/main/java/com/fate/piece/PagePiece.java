package com.fate.piece;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author ydc
 * @date 2020/12/24.
 */
@Data
@NoArgsConstructor
public class PagePiece {
    /**
     * load by user online
     * start from 1
     */
    protected Integer page;

    /**
     * load by user with order 1
     */
    public Integer pageSize;

    /**
     * load by user with order 2, but maybe only once
     */
    protected Integer total;

    /**
     * load after total
     */
    protected Integer totalPage;

    public PagePiece(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public PagePiece(Integer page, Integer pageSize) {
        this.page = page;
        this.pageSize = pageSize;
    }

    public void setTotal(Integer total){
        this.total = total;
        this.totalPage = new BigDecimal(total).divide(new BigDecimal(pageSize), 0, BigDecimal.ROUND_CEILING).intValue();
    }

    public void setTotal(PageHandler pageHandler) {
        Integer count = pageHandler.count();
        setTotal(count);
        for (int i = 1; i <= totalPage; i++) {
            this.page = i;
            pageHandler.callback(this);
        }
    }
}
