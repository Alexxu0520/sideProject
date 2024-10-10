package org.example;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("td_audit_request")
public class TdAuditRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private Long userId;
    private String input;
    private LocalDateTime updateTime;
    private LocalDateTime createTime;
}
