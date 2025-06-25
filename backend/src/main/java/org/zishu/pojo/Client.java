// 文件路径: backend/src/main/java/org/zishu/pojo/Client.java

package org.zishu.pojo;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * 客户信息实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Client {
    private Long clientId;
    private String name;
    private String type;
    private String phone;
    private String email;
    private String intent;
    private Long budget;
    private String roomRequirement;
    private Integer area;
    private String preferredArea;
    private String additionalRequirements;
    private BigDecimal budgetMin;
    private BigDecimal budgetMax;
    private Map<String, Object> preference;
    private LocalDateTime createdAt;
}
