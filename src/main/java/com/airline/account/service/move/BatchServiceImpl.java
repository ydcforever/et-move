package com.airline.account.service.move;

import com.airline.account.mapper.et.EtUplMapper;
import com.airline.account.mapper.et.SegmentMapper;
import com.airline.account.mapper.et.TaxMapper;
import com.airline.account.mapper.et.TicketMapper;
import com.airline.account.model.et.Segment;
import com.airline.account.model.et.Tax;
import com.airline.account.model.et.Ticket;
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
    private EtUplMapper etUplMapper;

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
