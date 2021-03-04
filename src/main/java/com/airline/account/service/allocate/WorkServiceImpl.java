package com.airline.account.service.allocate;

import com.airline.account.mapper.allocate.StatisticMapper;
import com.airline.account.mapper.allocate.WorkMapper;
import com.airline.account.model.allocate.Work;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ydc
 * @date 2021/2/24.
 */
@Service
public class WorkServiceImpl implements WorkService {

    @Autowired
    private WorkMapper workMapper;

    @Autowired
    private StatisticMapper statisticMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Value("${sql.insert.work}")
    private String insertWorkSql;

    @Value("${sql.update.work.running}")
    private String updateWorkRunningSql;

    @Override
    public void preparedNewFile(String tableName, String dateCol) {
        List<String> files = workMapper.findNewFile(tableName);
        List<Work> newWorks = new ArrayList<>();
        for (String file : files){
            statisticMapper.statistic(tableName, file, dateCol);
            Work work = new Work(tableName, file);
            work.setHasStatistic("Y");
            newWorks.add(work);
        }
        insertWork(newWorks);
    }

    @Override
    public void insertWork(List<Work> works) {
        jdbcTemplate.batchUpdate(insertWorkSql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Work work = works.get(i);
                ps.setString(1, work.getTableName());
                ps.setString(2, work.getFileName());
                ps.setString(3, work.getHasStatistic());
                ps.setString(4, work.getRunModel());
                ps.setString(5, work.getRunning());
            }

            @Override
            public int getBatchSize() {
                return works.size();
            }
        });
    }

    @Override
    public void updateWorkRunning(String tableName) {
        List<Work> works = workMapper.query(tableName);
        jdbcTemplate.batchUpdate(updateWorkRunningSql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Work work = works.get(i);
                ps.setString(1, work.getFileName());
                ps.setString(2, work.getFileName());
            }

            @Override
            public int getBatchSize() {
                return works.size();
            }
        });
    }
}
