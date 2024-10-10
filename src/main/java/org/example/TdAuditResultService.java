package org.example;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TdAuditResultService {

    @Autowired
    private TdAuditResultMapper tdAuditResultMapper;

    // Save method for TdAuditResult
    public void save(TdAuditResult auditResult) {
        tdAuditResultMapper.insert(auditResult);
    }

    // Return a LambdaQueryWrapper for TdAuditResult
    public LambdaQueryWrapper<TdAuditResult> lambdaQuery() {
        return new LambdaQueryWrapper<>();
    }

    // A method to list audit results by userId
    public List<TdAuditResult> getAuditResultsByUserId(Long userId) {
        return tdAuditResultMapper.selectList(
                new LambdaQueryWrapper<TdAuditResult>()
                        .eq(TdAuditResult::getUserId, userId)
        );
    }
}
