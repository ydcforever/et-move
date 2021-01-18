package com.airline.account.service.move;

import com.airline.account.mapper.et.MoveLogMapper;
import com.airline.account.model.et.MoveLog;
import com.airline.account.model.et.Relation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author ydc
 * @date 2021/1/15.
 */
@Service
public class RefundUseServiceImpl implements RefundUseService {

    @Value("${sql.insert.refund}")
    private String sqlInsertRefund;

    @Value("${sql.update.refund}")
    private String sqlUpdateRefund;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private MoveLogMapper moveLogMapper;

    @Override
    public void insertRefundWithUpdate(String logGroup, Relation refund) {
        try{
            jdbcTemplate.update(sqlInsertRefund, new PreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps) throws SQLException {
                    ps.setString(1, refund.getOperateDocumentCarrierIataNo());
                    ps.setString(2, refund.getOperateDocumentNo());
                    ps.setString(3, refund.getOperateIssueDate());
                    ps.setString(4, refund.getCouponUseIndicator());
                }
            });
        } catch (DuplicateKeyException ignore){

        } catch (Exception e) {
            String tktn = refund.getOperateDocumentCarrierIataNo() + refund.getOperateDocumentNo();
            MoveLog moveLog = new MoveLog(logGroup, tktn, e.getMessage());
            moveLogMapper.insertLog(moveLog);
        }
    }
}
