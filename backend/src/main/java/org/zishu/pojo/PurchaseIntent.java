package org.zishu.pojo;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 购房意向实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseIntent {
    private Long intentId;     // 意向ID（主键）
    private Long clientId;     // 客户ID（外键）
    private Long houseId;      // 房源ID（外键）
    private BigDecimal price;  // 意向价格
    private String paymentPlan; // 付款计划（如“首付30%+贷款70%”）
    private String status;     // 状态，默认值：待处理
    private LocalDateTime createdAt; // 创建时间，默认值：当前时间
}
