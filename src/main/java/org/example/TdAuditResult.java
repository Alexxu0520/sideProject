package org.example;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("td_audit_result") // 指定 "td_audit_result" 表进行映射
public class TdAuditResult implements Serializable {

    private static final long serialVersionUID = 1L; // 序列化ID，用于确保对象在序列化和反序列化过程中保持一致

    // 每个审核结果的唯一标识符
    private Long id;

    // 发起审核请求的用户的唯一ID
    private Long userId;

    // 对应的审核请求ID，用于与 td_audit_request 表关联
    private Long requestId;

    // 存储用户提交的原始数据或文本
    private String input;

    // 存储此次审核所生成的各种标签 (如 "暴力" "色情" 等)
    private String tags;

    // 此条记录最后一次被更新的时间
    private LocalDateTime updateTime;

    // 此条记录创建的时间
    private LocalDateTime createTime;
}