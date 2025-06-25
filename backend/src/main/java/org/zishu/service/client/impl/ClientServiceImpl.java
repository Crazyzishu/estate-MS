package org.zishu.service.client.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zishu.mapper.ClientMapper;
import org.zishu.pojo.Client;
import org.zishu.pojo.ClientQueryParam;
import org.zishu.pojo.PageResult;
import org.zishu.service.client.ClientService;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ClientServiceImpl implements ClientService {

    @Autowired
    private ClientMapper clientMapper;

    @Override
    public PageResult<Client> page(ClientQueryParam clientQueryParam) {
        //1. 设置分页信息
        PageHelper.startPage(clientQueryParam.getPageNum(),clientQueryParam.getPageSize());

        //2. 执行查询
        List<Client> clientList = clientMapper.list(clientQueryParam);

        //3. 解析查询结果，并封装到PageResult
        Page<Client> p = (Page<Client>) clientList;
        return new PageResult<Client>(p.getTotal(), p.getResult());

    }

    /**
     * 新增客户信息
     * @param client
     */
    @Override
    public void save(Client client) {
        //1.补全基础属性
        client.setCreatedAt(LocalDateTime.now());

        //2.保存客户信息
        clientMapper.save(client);
    }

    /**
     * 修改客户信息
     * @param client
     */
    @Override
    public void update(Client client) {
        clientMapper.update(client);
    }

    /**
     * 删除客户信息
     * @param id
     */
    @Override
    public void delete(Integer id) {
        clientMapper.delete(id);
    }

    /**
     * 根据id查询客户信息
     * @param id
     * @return
     */
    @Override
    public Client getById(Integer id) {
        return clientMapper.getById(id);
    }
}
