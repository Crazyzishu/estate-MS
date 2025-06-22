package org.zishu.service.admin;

import org.zishu.pojo.House;
import org.zishu.pojo.HouseQueryParam;
import org.zishu.pojo.PageResult;

public interface HouseService {

    /**
     * 分页查询
     * @param houseQueryParam
     * @return
     */
    PageResult<House> page(HouseQueryParam houseQueryParam);

    /**
     * 新增房源信息
     * @param house
     */
    void save(House house) ;

    /**
     * 修改房源信息
     * @param house
     */
    void update(House house);

    /**
     * 删除房源信息
     * @param id
     */
    void delete(Integer id);

    House getById(Integer id);
}
