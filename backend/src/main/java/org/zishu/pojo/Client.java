// 文件路径: backend/src/main/java/org/zishu/pojo/Client.java

package org.zishu.pojo;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 客户信息实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Client {
    private Long clientId;          // 客户ID（主键）
    private String name;            // 客户姓名
    private String phone;           // 手机号（唯一索引）
    private String email;           // 邮箱
    private BigDecimal budgetMin;   // 购房预算最低值
    private BigDecimal budgetMax;   // 购房预算最高值
    private String preference;      // 需求标签（JSON格式，如{"type":"住宅","area":"80-120"}）
    private LocalDateTime createdAt; // 注册时间，默认值：当前时间
}
