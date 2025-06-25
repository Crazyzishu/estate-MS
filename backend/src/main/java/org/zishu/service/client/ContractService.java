package org.zishu.service.client;

import org.zishu.pojo.Contract;
import org.zishu.pojo.ContractQueryParam;
import org.zishu.pojo.PageResult;

import java.util.List;
import java.util.Map;

/**
 * @author Blank
 * @version 1.0
 */
public interface ContractService {

    /*
    * 分页查询
     */
    PageResult<Contract> page(ContractQueryParam contractQueryParam);

    /*
    * 查询全部用户
     */
    List<Map<Integer,String>> getAllClients();

    /*
    * 查询全部房屋地址
     */
    List<Map<Integer, String>> getAllHouses();

    /*
    * 查询全部管理员
     */
    List<Map<Integer, String>> getAllManagers();

    /**
     * 新增合同
     * @param contract
     */
    void insert(Contract contract);

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
