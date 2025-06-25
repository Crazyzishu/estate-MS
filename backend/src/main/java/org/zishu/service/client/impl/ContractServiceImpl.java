package org.zishu.service.client.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zishu.mapper.ContractMapper;
import org.zishu.pojo.Client;
import org.zishu.pojo.Contract;
import org.zishu.pojo.ContractQueryParam;
import org.zishu.pojo.PageResult;
import org.zishu.service.client.ContractService;

import java.util.List;
import java.util.Map;

/**
 * @author Blank
 * @version 1.0
 */
@Service
public class ContractServiceImpl implements ContractService {


    @Autowired
    private ContractMapper contractMapper;


    @Override
    public PageResult<Contract> page(ContractQueryParam contractQueryParam) {
        //1. 设置分页信息
        PageHelper.startPage(contractQueryParam.getPageNum(),contractQueryParam.getPageSize());

        //2. 执行查询
        List<Contract> contractList = contractMapper.list(contractQueryParam);

        //3. 解析查询结果，并封装到PageResult
        Page<Contract> p = (Page<Contract>) contractList;
        return new PageResult<Contract>(p.getTotal(), p.getResult());
    }

    @Override
    public List<Map<Integer,String>> getAllClients() {
        return contractMapper.getAllClients();
    }

    @Override
    public List<Map<Integer, String>> getAllHouses() {
        return contractMapper.getAllHouses();
    }

    @Override
    public List<Map<Integer, String>> getAllManagers() {
        return contractMapper.getAllManagers();
    }

    @Override
    public void insert(Contract contract) {
        contractMapper.insert(contract);
    }

    @Override
    public Contract getById(Integer id) {
        return contractMapper.getById(id);
    }

    @Override
    public void update(Contract contract) {
        contractMapper.update(contract);
    }

    @Override
    public void delete(Integer id) {
        contractMapper.delete(id);
    }

}
