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
public class HouseQueryParam {
  private Integer pageNum=1;//分页页码
  private Integer pageSize=10;//每页数量
  private String address;//地址关键词
  private  Double minPrice;//最低价格
  private  Double maxPrice;//最高价格
  private String status;//房源状态（待审核/在售/已售）

  public Integer getPageNum() {
    return pageNum;
  }

  public Integer getPageSize() {
    return pageSize;
  }
}
