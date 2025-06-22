package org.zishu.pojo;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 客户预约看房实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Appointment {
    private Long appointmentId; // 预约ID（主键）
    private Long clientId;      // 客户ID（外键）
    private Long houseId;       // 房源ID（外键）
    private LocalDateTime visitTime; // 预约看房时间
    private Long managerId;     // 负责销售员ID（外键）
    private String status;      // 状态，默认值：待确认
    private String remark;      // 预约备注
}
