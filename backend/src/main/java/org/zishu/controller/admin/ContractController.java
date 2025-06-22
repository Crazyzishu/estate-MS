// 文件路径: backend/src/main/java/org/zishu/controller/admin/ContractController.java
package org.zishu.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.zishu.pojo.Contract;
import org.zishu.service.admin.ContractService;
import java.util.List;

@RestController
@RequestMapping("/api/contracts")
public class ContractController {

    @Autowired
    private ContractService contractService;

    // 获取所有合同
    @GetMapping
    public List<Contract> getAllContracts() {
        return contractService.getAllContracts();
    }

    // 获取单个合同
    @GetMapping("/{id}")
    public Contract getContract(@PathVariable("id") Long contractId) {
        return contractService.getContractById(contractId);
    }

    // 新增合同
    @PostMapping
    public void createContract(@RequestBody Contract contract) {
        contractService.addContract(contract);
    }

    // 更新合同
    @PutMapping("/{id}")
    public void updateContract(@PathVariable("id") Long contractId, @RequestBody Contract contract) {
        contract.setContractId(contractId);
        contractService.updateContract(contract);
    }

    // 删除合同
    @DeleteMapping("/{id}")
    public void deleteContract(@PathVariable("id") Long contractId) {
        contractService.deleteContract(contractId);
    }
}
