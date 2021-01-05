package com.airline.account.service.move;

import com.airline.account.model.et.Upl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 * @author ydc
 * @date 2020/12/2
 */
@Service
public interface UplService {

    /**
     * 国内日数据 承运
     * @param airline 航司
     * @param ticketNo 票号
     * @param issueDate 出票日期
     * @return List
     */
    List<Upl> moveDdpUpl(String airline, String ticketNo, String issueDate);

    /**
     * 国际日数据 承运
     *
     * @param airline 航司
     * @param ticketNo 票号
     * @param issueDate 出票日期
     * @return List
     */
    List<Upl> moveDipUpl(String airline, String ticketNo, String issueDate);

    /**
     * 国内月数据 承运
     *
     * @param airline 航司
     * @param ticketNo 票号
     * @param issueDate 出票日期
     * @return List
     */
    List<Upl> moveMdpUpl(String airline, String ticketNo, String issueDate);

    /**
     * 国际月数据 承运
     *
     * @param airline 航司
     * @param ticketNo 票号
     * @param issueDate 出票日期
     * @return List
     */
    List<Upl> moveMipUpl(String airline, String ticketNo, String issueDate);

}
