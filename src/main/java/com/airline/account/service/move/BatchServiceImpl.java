package com.airline.account.service.move;

import com.airline.account.model.et.Segment;
import com.airline.account.model.et.Tax;
import com.airline.account.model.et.Ticket;
import com.airline.account.utils.EtFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void insertTicket(List<Ticket> tickets) throws Exception {
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
    }

    @Override
    public void insertSegment(List<Segment> segments) throws Exception {
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
    }

    @Override
    public void insertTax(List<Tax> taxes) throws Exception {
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
                System.out.println(fields[i].getName());
                e.printStackTrace();
            } finally {
                fields[i].setAccessible(false);
            }
        }
    }
}
