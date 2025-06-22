package org.zishu.pojo;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * 房源图片实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HouseImage {
    private Long imageId;   // 图片ID（主键）
    private Long houseId;   // 关联房源ID（外键）
    private String imageUrl; // 图片URL（支持HTTPS）
    private Boolean isMain; // 是否为主图，默认值：false
}
