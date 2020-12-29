package com.airline.account.service.move;

import org.springframework.stereotype.Service;

/**
 * Created by ydc on 2020/12/2.
 */
@Service
public interface UplService {

    void moveDDpUpl(String cxr, String doc, String issueDate);

    void moveDIpUpl(String cxr, String doc, String issueDate);

}
