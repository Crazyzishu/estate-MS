package org.zishu.pojo;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Contract {
    private Long contractId;     // 合同ID（主键）
    private Long clientId;       // 客户ID（外键）
    private Long houseId;        // 房源ID（外键）
    private Long managerId;      // 销售ID（外键）
    private BigDecimal totalAmount; // 合同总金额
    private String paymentPlan;  // 付款计划（如“首付30%+贷款70%”）
    private LocalDateTime signTime; // 签约时间
    private String status;       // 状态，默认值：待签约
    private String contractFile; // 电子合同文件路径或Base64编码
}
