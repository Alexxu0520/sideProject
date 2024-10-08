package org.example;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("td_audit_request") // 指定 "td_audit_request" 表进行映射
public class TdAuditRequest implements Serializable {

    private static final long serialVersionUID = 1L; // 序列化ID，用于确保对象一致性

    // 每个审核请求记录的唯一标识符
    private Long id;

    // 发起请求的用户的标识符
    private Long userId;

    // 提交进行审核的原始文本或数据
    private String input;

    // 记录最后一次更新的时间
    private LocalDateTime updateTime;

    // 记录最初创建的时间
    private LocalDateTime createTime;
}