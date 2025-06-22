package org.zishu.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Admin {
    private Long adminId;           // 管理员ID（主键）
    private String username;        // 登录账号
    private String password;        // 加密后的密码
    private String realName;        // 真实姓名
    private String email;           // 邮箱
    private String phone;           // 手机号
    private String role;            // 角色（管理员/超级管理员），默认值：管理员
    private Boolean isActive;       // 是否启用（false=禁用，true=启用），默认值：true
    private LocalDateTime createdAt; // 创建时间，默认值：当前时间
    private LocalDateTime lastLogin; // 最近登录时间
    private String avatarUrl;       // 头像URL
}
