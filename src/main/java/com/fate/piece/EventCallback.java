package com.fate.piece;

/**
 * @author ydc
 * @date 2020/12/25.
 */
public interface EventCallback {

    /**
     * callback handle event
     *
     * @param eventPiece event
     */
    default void handle(EventPiece eventPiece){

    };
}
