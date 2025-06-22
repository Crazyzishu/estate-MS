package org.zishu.controller.admin;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.zishu.pojo.House;
import org.zishu.pojo.HouseQueryParam;
import org.zishu.pojo.PageResult;
import org.zishu.pojo.Result;
import org.zishu.service.admin.HouseService;

@Slf4j
@RestController
@RequestMapping("/api/houses")
public class HouseController {

    @Autowired
    private HouseService houseService;

    /**
     * 新增房源信息
     * @param house
     * @return
     */
    @PostMapping
    public Result save(@RequestBody House house) {
        log.info("新增房源信息: {}",house);
        houseService.save(house);
        return Result.success();
    }

    /**
     * 分页查询
     * 1.请求路径: /api/houses/query
     * 2.请求方式: GET
     * 3.请求参数: pageNum,pageSize,address,minPrice,maxPrice,status
     * @param houseQueryParam
     * @return
     */
    @GetMapping("/query")
    public Result page(HouseQueryParam houseQueryParam) {
        log.info("分页查询: {}",houseQueryParam);
        PageResult<House> pageResult=houseService.page(houseQueryParam);
        return Result.success(pageResult);
    }

    /**
     * 根据id查询房源信息
     */
    @GetMapping("/{id}")
    public Result get(@PathVariable Integer id) {
        log.info("根据id查询房源信息: {}",id);
        House house = houseService.getById(id);
        return Result.success(house);
    }

    /**
     * 修改房源信息
     * @param house
     * @return
     */
    @PutMapping
    public Result update(@RequestBody House house) {
        log.info("修改房源信息: {}",house);
        houseService.update(house);
        return Result.success();
    }

    /**
     * 删除房源信息
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        log.info("删除房源信息: {}",id);
        houseService.delete(id);
        return Result.success();
    }
}