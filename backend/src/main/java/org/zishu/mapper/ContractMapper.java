// 文件路径: backend/src/main/java/org/zishu/mapper/ContractMapper.java
package org.zishu.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.zishu.pojo.Contract;
import java.util.List;

@Mapper
public interface ContractMapper {
    List<Contract> getAllContracts();              // 获取所有合同
    Contract getContractById(Long contractId);     // 根据ID获取合同
    void updateContract(Contract contract);        // 更新合同
    void deleteContract(Long contractId);          // 删除合同
    void insertContract(Contract contract);        // 插入新合同
}
