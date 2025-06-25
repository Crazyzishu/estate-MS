package org.zishu.service.client;

import org.zishu.pojo.Client;
import org.zishu.pojo.ClientQueryParam;
import org.zishu.pojo.PageResult;

public interface ClientService {
    /**
     * 条件分页查询
     * @param clientQueryParam
     * @return
     */
    PageResult<Client> page(ClientQueryParam clientQueryParam);

    /**
     * 新增客户信息
     * @param client
     */
    void save(Client client);

    /**
     * 修改客户信息
     * @param client
     */
    void update(Client client);

    /**
     * 根据id删除客户信息
     * @param id
     */
    void delete(Integer id);

    /**
     * 根据id查询客户信息
     * @param id
     * @return
     */
    Client getById(Integer id);
}
