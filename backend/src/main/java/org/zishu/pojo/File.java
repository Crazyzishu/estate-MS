package org.zishu.pojo;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 文件存储实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class File {
    private Long fileId;        // 文件ID（主键）
    private String fileUrl;      // 文件路径或Base64编码
    private Long relatedId;     // 关联的房源ID或其他业务ID
    private String fileType;    // 文件类型（产权证明/合同/广告/其他）
    private LocalDateTime createdAt; // 创建时间，默认值：当前时间
}
