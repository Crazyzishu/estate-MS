package org.zishu.pojo;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 销售员实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Manager {
    private Long managerId;         // 销售员ID（主键）
    private String username;        // 登录账号
    private String password;        // 加密后的密码
    private String realName;        // 真实姓名
    private String email;           // 邮箱
    private String phone;           // 手机号
    private String serviceArea;     // 服务区域（如朝阳区、海淀区）
    private Integer starLevel;      // 经纪人星级，默认值：3
    private LocalDateTime createdAt; // 入职时间，默认值：当前时间
    private String avatarUrl;       // 头像URL
    private Boolean isActive;       // 是否启用，默认值：true
}
