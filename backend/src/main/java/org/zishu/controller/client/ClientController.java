package org.zishu.controller.client;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.zishu.pojo.*;
import org.zishu.service.client.ClientService;

@Slf4j
@RestController
@RequestMapping("/api/clients")
public class ClientController {

    @Autowired
    private ClientService clientService;

    /**
     * 分页查询
     */
    @GetMapping("query")
    public Result page(ClientQueryParam clientQueryParam) {
        log.info("分页查询,参数:{}",clientQueryParam);
        PageResult<Client> pageResult=clientService.page(clientQueryParam);
        return Result.success(pageResult);
    }

    @PostMapping
    public Result save(@RequestBody Client client) {
        log.info("保存,参数:{}",client);
        clientService.save(client);
        return Result.success();
    }

    @PutMapping
    public Result update(@RequestBody Client client) {
        log.info("更新,参数:{}",client);
        clientService.update(client);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        log.info("删除,参数:{}",id);
        clientService.delete(id);
        return Result.success();
    }

    /**
     * 根据id查询
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result get(@PathVariable Integer id) {
        log.info("根据id查询,参数:{}",id);
        Client client = clientService.getById(id);
        return Result.success(client);
    }
}
