package org.zishu.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Blank
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientQueryParam {
    private Integer pageNum=1;//分页页码
    private Integer pageSize=10;//每页数量
    private String name;//客户姓名
    private Double type;//客户类型
    private Double intent;//客户购买意向
}
