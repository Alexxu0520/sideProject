package org.example;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("td_audit_result")
public class TdAuditResult implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Long userId;
    private Long requestId;
    private String input;
    private String tags;
    private LocalDateTime updateTime;
    private LocalDateTime createTime;
}
