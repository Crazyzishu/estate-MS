package org.zishu.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.zishu.pojo.Client;
import org.zishu.pojo.ClientQueryParam;

import java.util.List;

/**
 * @author Blank
 * @version 1.0
 */
@Mapper
public interface ClientMapper {

    /**
     * 分页查询
     * @param clientQueryParam
     * @return
     */
    List<Client> list(ClientQueryParam clientQueryParam);

    /**
     *  新增客户信息
     * @param client
     */
    void save(Client client);

    /**
     * 修改客户信息
     * @param client
     */
    void update(Client client);

    /**
     * 删除客户信息
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
