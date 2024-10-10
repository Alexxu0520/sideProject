package org.example;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.FieldFill;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("td_audit_response")
public class TdAuditResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long userId;
    private Long requestId;
    private Long orgId;
    private Integer model;
    private Integer flagged;

    private Integer sexual;
    private Integer hate;
    private Integer harassment;
    private Integer selfHarm;
    private Integer sexualMinors;
    private Integer hateThreatening;
    private Integer violenceGraphic;
    private Integer selfHarmIntent;
    private Integer selfHarmInstructions;
    private Integer harassmentThreatening;
    private Integer violence;

    private Long sexualScore;
    private Long hateScore;
    private Long harassmentScore;
    private Long selfHarmScore;
    private Long sexualMinorsScore;
    private Long hateThreateningScore;
    private Long violenceGraphicScore;
    private Long selfHarmIntentScore;
    private Long selfHarmInstructionsScore;
    private Long harassmentThreateningScore;
    private Long violenceScore;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
