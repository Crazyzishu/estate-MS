package org.zishu.controller.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.zishu.pojo.Contract;
import org.zishu.pojo.ContractQueryParam;
import org.zishu.pojo.PageResult;
import org.zishu.pojo.Result;
import org.zishu.service.client.ContractService;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/contracts")
public class ContractController {

    @Autowired
    private ContractService contractService;

    /**
     * 分页查询
     * @param contractQueryParam
     * @return
     */
    @GetMapping("query")
    public Result page(ContractQueryParam contractQueryParam) {
        log.info("分页查询: {}",contractQueryParam);
        PageResult<Contract> pageResult=contractService.page(contractQueryParam);
        return Result.success(pageResult);
    }

    @GetMapping("/getClients")
    public Result getAllClients() {
        log.info("查询全部用户");
        List<Map<Integer,String>> clientList = contractService.getAllClients();
        return Result.success(clientList);
    }

    @GetMapping("/getHouses")
    public Result getAllHouses() {
        log.info("查询全部房源地址");
        List<Map<Integer,String>> houseList = contractService.getAllHouses();
        return Result.success(houseList);
    }

    @GetMapping("/getManagers")
    public Result getAllManagers() {
        log.info("查询全部管理员");
        List<Map<Integer,String>> managerList = contractService.getAllManagers();
        return Result.success(managerList);
    }

    @PostMapping
    public Result save(@RequestBody Contract contract) {
        log.info("保存合同: {}", contract);
        contractService.insert(contract);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result get(@PathVariable Integer id) {
        log.info("根据id查询合同信息: {}", id);
        Contract contract = contractService.getById(id);
        return Result.success(contract);
    }

    @PutMapping
    public Result update(@RequestBody Contract contract) {
        log.info("修改合同信息: {}", contract);
        contractService.update(contract);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        log.info("删除合同: {}", id);
        contractService.delete(id);
        return Result.success();
    }
}
