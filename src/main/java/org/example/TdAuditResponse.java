package org.example;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("td_audit_response") // 指定 "td_audit_response" 表进行映射
public class TdAuditResponse implements Serializable {

    private static final long serialVersionUID = 1L; // 序列化ID，用于确保对象一致性

    // 每个审核响应的唯一标识符
    private Long id;

    // 发起审核请求的用户的唯一ID
    private Long userId;

    // 对应的审核请求ID，用于与 td_audit_request 表关联
    private Long requestId;

    // 组织或公司的唯一ID
    private Long orgId;

    // 使用的审核模型的标识符，用于指明采用哪种审核策略
    private Integer model;

    // 内容是否被标记为有问题，1 表示有问题，0 表示无问题
    private Integer flagged;

    // 各种敏感或不当内容的类型标识，1 表示存在，0 表示不存在
    private Integer sexual;              // 性相关内容
    private Integer hate;                // 仇恨言论
    private Integer harassment;          // 骚扰行为
    private Integer selfHarm;            // 自我伤害
    private Integer sexualMinors;        // 涉及未成年人的性相关内容
    private Integer hateThreatening;     // 带有威胁性的仇恨言论
    private Integer violenceGraphic;     // 图解暴力
    private Integer selfHarmIntent;      // 自我伤害意图
    private Integer selfHarmInstructions;// 自我伤害的具体指导
    private Integer harassmentThreatening;// 带有威胁性的骚扰行为
    private Integer violence;            // 暴力行为

    // 各种敏感或不当内容的评分，评分越高表示该类型内容越明显或严重
    private Long sexualScore;             // 性相关内容的评分
    private Long hateScore;               // 仇恨言论的评分
    private Long harassmentScore;         // 骚扰行为的评分
    private Long selfHarmScore;           // 自我伤害的评分
    private Long sexualMinorsScore;       // 涉及未成年人的性相关内容的评分
    private Long hateThreateningScore;    // 带有威胁性的仇恨言论的评分
    private Long violenceGraphicScore;    // 图解暴力的评分
    private Long selfHarmIntentScore;     // 自我伤害意图的评分
    private Long selfHarmInstructionsScore; // 自我伤害具体指导的评分
    private Long harassmentThreateningScore; // 带有威胁性的骚扰行为的评分
    private Long violenceScore;           // 暴力行为的评分

    // 此条记录最后一次被更新的时间
    private LocalDateTime updateTime;

    // 此条记录创建的时间
    private LocalDateTime createTime;
}