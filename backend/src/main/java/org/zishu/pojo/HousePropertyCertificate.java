package org.zishu.pojo;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * 房源产权证明实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HousePropertyCertificate {
    private Long certificateId;      // 产权证明ID（主键）
    private Long houseId;            // 关联房源ID（外键）
    private String fileUrl;          // 文件路径或Base64编码
    private LocalDate issueDate;     // 发证日期
    private String issuingAuthority; // 发证机构
}
