package org.zishu.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.zishu.pojo.House;
import org.zishu.pojo.HouseQueryParam;


import java.util.List;

@Mapper
public interface HouseMapper {

    /**
     * 新增房源信息
     * @param house
     */
    void insert(House house);

    /**
     * 分页查询
     * @param houseQueryParam
     * @return
     */
    List<House> list(HouseQueryParam houseQueryParam);

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
