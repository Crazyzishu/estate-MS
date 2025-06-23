package org.zishu.pojo;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 投诉管理实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Complaint {
    private Long complaintId;   // 投诉ID（主键）
    private Long clientId;      // 客户ID（外键）
    private Long houseId;
    private String content;     // 投诉内容
    private String contact;     // 联系方式（手机号或邮箱）
    private String status;      // 状态，默认值：待处理
    private LocalDateTime createdAt; // 创建时间，默认值：当前时间
    private LocalDateTime updateAt;
}
