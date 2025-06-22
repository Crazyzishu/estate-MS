package org.zishu.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class House {
    private Integer houseId;
    private String type;
    private String address;
    private BigDecimal price;
    private BigDecimal area;
    private String roomType;
    private String description;
    private String status;
    private Long managerId;
    private String images;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
