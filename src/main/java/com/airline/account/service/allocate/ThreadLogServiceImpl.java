package com.airline.account.service.allocate;

import com.airline.account.model.allocate.AllocateSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

/**
 * @author ydc
 * @date 2021/2/24.
 */
@Service
public class ThreadLogServiceImpl implements ThreadLogService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Value("${sql.insert.thread}")
    private String insertThread;

    @Value("${sql.update.thread}")
    private String updateThread;

    @Autowired
    private StatisticService statisticService;

    @Override
    public void insertLog(AllocateSource allocateSource) {
        jdbcTemplate.update(insertThread,
                allocateSource.getFileName(),
                allocateSource.getIssueDate(),
                allocateSource.getStart(),
                allocateSource.getThreadId());
        statisticService.beginPiece(allocateSource);
    }

    @Override
    public void updateLog(AllocateSource allocateSource) {
        jdbcTemplate.update(updateThread,
                allocateSource.getEnd(),
                allocateSource.getStatus(),
                allocateSource.getMessage(),
                allocateSource.getFileName(),
                allocateSource.getIssueDate(),
                allocateSource.getStart());
        statisticService.finishPiece(allocateSource);
    }


}
