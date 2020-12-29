package com.fate.piece;

import lombok.Data;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.util.Date;

/**
 * @author ydc
 * @date 2020/12/25.
 */
@Data
public class EventPiece {

    /**
     * event start time
     */
    protected final String start;

    /**
     * the temporary time of running
     */
    protected String follow;

    public EventPiece() {
        this.start = currentDate();
    }

    public void follow(EventCallback callback){
        this.follow = currentDate();
        callback.handle(this);
    }

    private String currentDate(){
        return DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss");
    }
}

