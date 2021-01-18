package com.airline.account.service.move;

import com.airline.account.model.et.Relation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * @author ydc
 * @date 2021/1/18.
 */
@Service
public class StatusServiceImpl implements StatusService {

    @Value("${sql.update.segment.status}")
    private String sqlUpdateSegmentStatus;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void updateSegmentStatus(List<Relation> relations) throws Exception {
        jdbcTemplate.batchUpdate(sqlUpdateSegmentStatus, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Relation relation = relations.get(i);
                ps.setString(1, relation.getCouponStatus());
                ps.setString(2, relation.getOperateDocumentCarrierIataNo());
                ps.setString(3, relation.getOperateDocumentNo());
                ps.setInt(4, relation.getCouponNo());
            }

            @Override
            public int getBatchSize() {
                return relations.size();
            }
        });
    }

    @Override
    public void updateSegmentStatus(Relation relation) throws Exception {
        jdbcTemplate.update(sqlUpdateSegmentStatus, new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setString(1, relation.getCouponStatus());
                ps.setString(2, relation.getOperateDocumentCarrierIataNo());
                ps.setString(3, relation.getOperateDocumentNo());
                ps.setInt(4, relation.getCouponNo());
            }
        });
    }
}
