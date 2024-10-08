package org.example;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("td_user") // 指定 "td_user" 表进行映射
public class TdUser implements Serializable {

    private static final long serialVersionUID = 1L; // 序列化ID，用于确保对象在序列化和反序列化过程中保持一致

    private Long id; // 用户的唯一标识符
    private String openid; // 用户的 OpenID，通常用于第三方登录
    private String nickname; // 用户的昵称
    private String avatar; // 用户头像的 URI
    private String sessionKey; // 用于用户会话管理的密钥
    private String account; // 用户的账号，通常是邮箱或手机号
    private String password; // 用户的密码
    private String tag; // 用户的标签或角色 (如管理员、普通用户等)
    private Integer dailyPoint; // 用户的日积分
    private Integer totalPoint; // 用户的总积分
    private LocalDate lastRefreshPoint; // 最后一次刷新积分的日期
    private LocalDateTime updateTime; // 此条记录最后一次被更新的时间
    private LocalDateTime createTime; // 此条记录创建的时间
}