package org.zishu.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ComplaintQueryParam {
    private Integer pageNum=1;//分页页码
    private Integer pageSize=10;//每页数量
    private String content;
    private Long complaintId;
    private String name;
    private Long houseId;
    private String status;
}
