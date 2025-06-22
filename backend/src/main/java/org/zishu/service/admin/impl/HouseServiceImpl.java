package org.zishu.service.admin.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zishu.mapper.HouseMapper;
import org.zishu.pojo.House;
import org.zishu.pojo.HouseQueryParam;
import org.zishu.pojo.PageResult;
import org.zishu.service.admin.HouseService;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class HouseServiceImpl implements HouseService {

    @Autowired
    private HouseMapper houseMapper;

    /**
     * 分页查询
     * @param houseQueryParam
     * @return
     */
    @Override
    public PageResult<House> page(HouseQueryParam houseQueryParam) {
        //1.设置分页参数
        PageHelper.startPage(houseQueryParam.getPageNum(),houseQueryParam.getPageSize());

        //2.执行查询
        List<House> houseList = houseMapper.list(houseQueryParam);

        //3.解析想查询结果,并封装到PageResult
        Page<House> p = (Page<House>) houseList;
        return new PageResult<House>(p.getTotal(), p.getResult());
    }

    /**
     * 新增房源信息
     * @param house
     */
    @Override
    public void save(House house) {
        //1.补全基础属性
        house.setCreatedAt(LocalDateTime.now());
        house.setUpdatedAt(LocalDateTime.now());

        //2.调用mapper方法插入数据
        houseMapper.insert(house);
    }

    /**
     * 修改房源信息
     * @param house
     */
    @Override
    public void update(House house) {
        //1.补全基础属性
        house.setUpdatedAt(LocalDateTime.now());

        //2.调用mapper方法更新数据
        houseMapper.update(house);
    }

    /**
     * 删除房源信息
     * @param id
     */
    @Override
    public void delete(Integer id) {
        houseMapper.delete(id);
    }

    @Override
    public House getById(Integer id) {
        return houseMapper.getById(id);

    }
}
