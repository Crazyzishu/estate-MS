// 文件路径: backend/src/main/java/org/zishu/service/admin/ContractService.java
package org.zishu.service.admin;

import org.zishu.pojo.Contract;
import java.util.List;

public interface ContractService {
    List<Contract> getAllContracts();             // 查询所有合同
    Contract getContractById(Long contractId);    // 查询单个合同
    void addContract(Contract contract);          // 添加合同
    void updateContract(Contract contract);       // 更新合同
    void deleteContract(Long contractId);         // 删除合同
}
