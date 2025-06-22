// 文件路径: backend/src/main/java/org/zishu/service/admin/impl/ContractServiceImpl.java
package org.zishu.service.admin.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zishu.mapper.ContractMapper;
import org.zishu.pojo.Contract;
import org.zishu.service.admin.ContractService;
import java.util.List;

@Service
public class ContractServiceImpl implements ContractService {

    @Autowired
    private ContractMapper contractMapper;

    @Override
    public List<Contract> getAllContracts() {
        return contractMapper.getAllContracts();
    }

    @Override
    public Contract getContractById(Long contractId) {
        return contractMapper.getContractById(contractId);
    }

    @Override
    public void addContract(Contract contract) {
        contractMapper.insertContract(contract);
    }

    @Override
    public void updateContract(Contract contract) {
        contractMapper.updateContract(contract);
    }

    @Override
    public void deleteContract(Long contractId) {
        contractMapper.deleteContract(contractId);
    }
}
