package com.fate.piece;

/**
 * @author ydc
 * @date 2020/12/25.
 */
public interface PageHandler {

    /**
     * the sum of all page
     *
     * @return all record
     */
    default Integer count(){
        return 0;
    };

    /**
     * handle the page when source split by page
     *
     * @param pagePiece page information
     */
    default void callback(PagePiece pagePiece) {

    }
}
