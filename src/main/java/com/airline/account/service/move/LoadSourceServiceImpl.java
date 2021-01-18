package com.airline.account.service.move;

import com.airline.account.utils.AllocateSource;
import com.fate.piece.PageHandler;
import com.fate.pool.PoolFinalHandler;
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

    private static final String FINISHED = "Y";

    @Override
    public void executeByFile(PoolFinalHandler finalHandler, AllocateSource allocateSource, PageHandler pageHandler) {
        String config = allocateSource.getConfigId();
        List<String> sources = getSource(config);
        for (String file : sources) {
            allocateSource.setFileName(file);
            allocateSource.setTotal(pageHandler);
            try {
                finalHandler.finalHandle();
            } catch (Exception e) {
                e.printStackTrace();
            }
//            updateStatus(config, file, FINISHED);
        }
    }

    @Override
    public void executeByDate(PoolFinalHandler finalHandler, AllocateSource allocateSource, PageHandler pageHandler) {
        String config = allocateSource.getConfigId();
        List<String> sources = getSource(config);
        for (String file : sources) {
            allocateSource.setFileName(file);
            List<String> issueDates = getIssueDates(allocateSource);
            for (String issue : issueDates) {
                allocateSource.setCurrentIssueDate(issue);
                allocateSource.setTotal(pageHandler);
                try {
                    finalHandler.finalHandle();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
//            updateStatus(config, file, FINISHED);
        }
    }

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
