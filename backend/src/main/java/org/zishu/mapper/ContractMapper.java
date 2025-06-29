package org.zishu.mapper;

import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.zishu.pojo.Contract;
import org.zishu.pojo.ContractQueryParam;

import java.util.List;
import java.util.Map;

/**
 * @author Blank
 * @version 1.0
 */
@Mapper
public interface ContractMapper {
    /**
     * 新增合同
     * @param contract
     */
    void insert(Contract contract);

    /**
     * 分页查询
     * @param contractQueryParam
     * @return
     */
    List<Contract> list(ContractQueryParam contractQueryParam);

    /**
     * 查询全部用户
     * @return
     */
    @MapKey("clientId")
    List<Map<Integer,String>> getAllClients();

    /**
     * 查询全部房屋地址
     * @return
     */
    @MapKey("houseId")
    List<Map<Integer, String>> getAllHouses();

    /**
     *  查询全部管理员姓名
     * @return
     */
    @MapKey("managerId")
    List<Map<Integer, String>> getAllManagers();

    /**
     * 根据id查询合同信息
     * @return
     */
    Contract getById(Integer id);

    /**
     * 根据id修改合同信息
     * @param contract
     */
    void update(Contract contract);

    /**
     * 根据id删除合同信息
     * @param id
     */
    void delete(Integer id);
}
