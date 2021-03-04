package com.airline.account.notice;

/**
 * @author ydc
 * @date 2021/1/27.
 */
public interface Notice {

    /**
     * send message to the personnel of operation and maintenance
     *
     * @param id      is account id, maybe group id
     * @param content is message
     */
    void send(String content, String... id);

}
