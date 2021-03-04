package com.airline.account.service.allocate;

import com.airline.account.mapper.allocate.StatisticMapper;
import com.airline.account.mapper.allocate.WorkMapper;
import com.airline.account.model.allocate.AllocateSource;
import com.airline.account.model.allocate.Work;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author ydc
 * @date 2021/2/25.
 */
@Service
public class StatisticServiceImpl implements StatisticService {

    @Autowired
    private WorkMapper workMapper;

    @Autowired
    private StatisticMapper statisticMapper;

    private static final String QUERY_RESOURCE = "select t.file_name, t.issue_date, " +
            "t.total, t.has_execute from MOVE_STATISTICS t, MOVE_CONFIG f " +
            "where t.file_name = f.file_name" +
            " and f.running = 'N' and f.table_name = ?" +
            "and (f.run_model = 'C' or (f.run_model = 'P' and t.has_execute = 'N'))";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public LinkedBlockingQueue<AllocateSource> getResource(AllocateSource allocateSource) {
        LinkedBlockingQueue<AllocateSource> queue = new LinkedBlockingQueue<>();
        statistic(allocateSource.getTableName(), allocateSource.getDateColName());
        jdbcTemplate.query(QUERY_RESOURCE, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                AllocateSource newSource = new AllocateSource();
                newSource.setPageSize(allocateSource.getPageSize());
                newSource.setTableName(allocateSource.getTableName());
                newSource.setFileName(rs.getString(1));
                newSource.setIssueDate(rs.getString(2));
                newSource.setTotal(rs.getInt(3));
                newSource.setHasExecute(rs.getString(4));
                queue.add(newSource);
            }
        }, allocateSource.getTableName());
        return queue;
    }

    @Override
    public void statistic(String tableName, String dateCol) {
        List<Work> waitWorks = workMapper.queryNoStatistic(tableName);
        for(Work work: waitWorks){
            try{
                statisticMapper.statistic(tableName, work.getFileName(), dateCol);
                workMapper.updateHasStatistic(work.getFileName());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void beginPiece(AllocateSource allocateSource) {
        statisticMapper.beginPiece(allocateSource);
    }

    @Override
    public void finishPiece(AllocateSource allocateSource) {
        statisticMapper.finishPiece(allocateSource);
    }
}
