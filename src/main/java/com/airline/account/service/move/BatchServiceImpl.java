package com.airline.account.service.move;

import com.airline.account.mapper.et.SegmentMapper;
import com.airline.account.mapper.et.TaxMapper;
import com.airline.account.mapper.et.TicketMapper;
import com.airline.account.mapper.et.UplMapper;
import com.airline.account.model.acca.Relation;
import com.airline.account.model.et.Segment;
import com.airline.account.model.et.Tax;
import com.airline.account.model.et.Ticket;
import com.airline.account.model.et.Upl;
import com.airline.account.utils.EtFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * @author ydc
 * @date 2020/12/23.
 */
@Service
public class BatchServiceImpl implements BatchService {

    @Value("${sql.insert.ticket}")
    private String sqlInsertTicket;

    @Value("${sql.insert.segment}")
    private String sqlInsertSegment;

    @Value("${sql.insert.tax}")
    private String sqlInsertTax;

    @Value("${sql.update.segment.status}")
    private String sqlUpdateSegmentStatus;

    @Value("${sql.insert.refund}")
    private String sqlInsertRefund;

    @Value("${sql.insert.exchange}")
    private String sqlInsertExchange;

    @Value("${sql.insert.upl}")
    private String sqlInsertUpl;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private TicketMapper ticketMapper;

    @Autowired
    private SegmentMapper segmentMapper;

    @Autowired
    private TaxMapper taxMapper;

    @Autowired
    private UplMapper uplMapper;

    @Override
    public void insertTicket(List<Ticket> tickets) throws Exception {
        try{
            jdbcTemplate.batchUpdate(sqlInsertTicket, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    Ticket ticket = tickets.get(i);
                    setPreparedStatement(ps, ticket);
                }

                @Override
                public int getBatchSize() {
                    return tickets.size();
                }
            });
        } catch (DuplicateKeyException e) {
            if(tickets.size() == 1) {
                ticketMapper.updateTicket(tickets.get(0));
            } else {
                throw e;
            }
        }
    }

    @Override
    public void insertSegment(List<Segment> segments) throws Exception {
        try{
            jdbcTemplate.batchUpdate(sqlInsertSegment, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    Segment segment= segments.get(i);
                    setPreparedStatement(ps, segment);
                }

                @Override
                public int getBatchSize() {
                    return segments.size();
                }
            });
        } catch (DuplicateKeyException e) {
            if(segments.size() == 1) {
                segmentMapper.updateSegment(segments.get(0));
            } else {
                throw e;
            }
        }
    }

    @Override
    public void insertTax(List<Tax> taxes) throws Exception {
        try{
            jdbcTemplate.batchUpdate(sqlInsertTax, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    Tax tax = taxes.get(i);
                    setPreparedStatement(ps, tax);
                }

                @Override
                public int getBatchSize() {
                    return taxes.size();
                }
            });
        } catch (DuplicateKeyException e){
            if(taxes.size() == 1) {
                taxMapper.updateTax(taxes.get(0));
            } else {
                throw e;
            }
        }
    }

    @Override
    public void updateSegmentStatus(List<String[]> relations) throws Exception {
        jdbcTemplate.batchUpdate(sqlUpdateSegmentStatus, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                String[] relation = relations.get(i);
                ps.setString(1, relation[3]);
                ps.setString(2, relation[0]);
                ps.setString(3, relation[1]);
                ps.setInt(4, EtFormat.intFormat(relation[2]));
            }

            @Override
            public int getBatchSize() {
                return relations.size();
            }
        });
    }

    @Override
    public void insertRefund(List<String[]> refunds) throws Exception {
        jdbcTemplate.batchUpdate(sqlInsertRefund, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                String[] refund = refunds.get(i);
                setPreparedStatement(preparedStatement, refund);
            }

            @Override
            public int getBatchSize() {
                return refunds.size();
            }
        });
    }

    /**
     * 连票查询处理--待办
     *
     * @param exchanges list of exchange
     * @throws Exception e
     */
    @Override
    public void insertExchange(List<Relation> exchanges) throws Exception {
        jdbcTemplate.batchUpdate(sqlInsertExchange, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Relation relation = exchanges.get(i);

//                String ticketNo = relation.getPrimaryTicketNo();
//                ps.setString(1, ticketNo.substring(0, 3));
//                ps.setString(2, ticketNo.substring(3));

                ps.setString(3, "");
                ps.setString(4, relation.getIssueDate());
                String orgTicketNo = relation.getOrgTicketNo();
                ps.setString(5, orgTicketNo.substring(0, 3));
                ps.setString(6, orgTicketNo.substring(3));
                ps.setString(7, "");
                ps.setString(8, relation.getOrgIssueDate());
                ps.setString(9, relation.getCouponStatus());
            }

            @Override
            public int getBatchSize() {
                return exchanges.size();
            }
        });
    }

    @Override
    public void insertUpl(List<Upl> uplList) throws Exception {
        try{
            jdbcTemplate.batchUpdate(sqlInsertUpl, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                    Upl upl = uplList.get(i);
                    setPreparedStatement(preparedStatement, upl);
                }

                @Override
                public int getBatchSize() {
                    return uplList.size();
                }
            });
        } catch(DuplicateKeyException e){
            if(uplList.size() == 1) {
                uplMapper.updateUpl(uplList.get(0));
            } else {
                throw e;
            }
        }
    }

    /**
     * 字符数组和SQL的占位符个数及顺序 必须一致
     * @param ps PreparedStatement
     * @param obj obj
     */
    private static void setPreparedStatement(PreparedStatement ps, String[] obj){
        for(int i = 0, len = obj.length; i < len; i++) {
            try {
                ps.setString(i + 1, obj[i]);
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
        }
    }

    /**
     * 对象属性和SQL的占位符个数及顺序 必须一致
     *
     * @param ps PreparedStatement
     * @param t object
     * @param <T> t
     */
    private static <T> void setPreparedStatement(PreparedStatement ps, T t){
        Field[] fields = t.getClass().getDeclaredFields();
        for(int i = 0, len = fields.length; i < len; i++){
            try {
                fields[i].setAccessible(true);
                Object value = fields[i].get(t);
                int pos = i + 1;
                Type type = fields[i].getGenericType();
                if ("java.lang.String".equals(type.getTypeName())) {
                    if(value != null) {
                        ps.setString(pos, value.toString());
                    } else {
                        ps.setString(pos, "");
                    }
                } else {
                    if(value != null) {
                        ps.setBigDecimal(pos, new BigDecimal(value.toString()));
                    } else {
                        ps.setBigDecimal(pos, BigDecimal.ZERO);
                    }
                }
            } catch (IllegalAccessException | SQLException e) {
                e.printStackTrace();
            } finally {
                fields[i].setAccessible(false);
            }
        }
    }
}
