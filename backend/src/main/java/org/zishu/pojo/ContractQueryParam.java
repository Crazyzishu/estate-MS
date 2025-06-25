package org.zishu.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author Blank
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContractQueryParam {
    private Integer pageNum=1;
    private Integer pageSize=10;
    private Long contractId;
    private String clientName;
    private Integer contractType;
    private String status;
}
