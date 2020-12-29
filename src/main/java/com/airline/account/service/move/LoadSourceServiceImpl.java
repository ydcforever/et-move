package com.airline.account.service.move;

import com.airline.account.utils.AllocateSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 * @author ydc
 * @date 2020/12/19
 */
@Service
public class LoadSourceServiceImpl implements LoadSourceService {

    @Value("${sql.query.source}")
    private String sqlQuerySource;

    @Value("${sql.update.source}")
    private String sqlUpdateSource;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<String> getSource(String module) {
        return jdbcTemplate.queryForList(sqlQuerySource, String.class, module);
    }

    @Override
    public void updateStatus(String module, String source, String status) {
        jdbcTemplate.update(sqlUpdateSource, status, module, source);
    }

    @Override
    public List<String> getIssueDates(AllocateSource allocateSource){
        return jdbcTemplate.queryForList(allocateSource.getIssueSql(), String.class, allocateSource.getFileName());
    }
}
