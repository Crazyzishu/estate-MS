package org.zishu.pojo;

import lombok.Data;

/**
 * 预约看房查询参数类（支持分页+筛选）
 */
@Data
public class AppointmentQueryParam {
    private int pageNum = 1;     // 当前页码
    private int pageSize = 10;   // 每页数量
    private Long clientId;       // 客户ID（可选）
    private Long houseId;        // 房源ID（可选）
    private Long managerId;      // 销售人员ID（可选）
    private String status;       // 状态（可选）
}
