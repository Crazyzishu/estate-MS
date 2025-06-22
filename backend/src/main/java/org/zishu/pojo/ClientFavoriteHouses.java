package org.zishu.pojo;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 客户收藏房源关联实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientFavoriteHouses {
    private Long clientId; // 客户ID（外键）
    private Long houseId;  // 房源ID（外键）
}
