package org.zishu.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.zishu.pojo.Admin;

import java.util.List;

@Mapper
public interface AdminMapper {
    void insert(Admin admin);
    void update(Admin admin);
    void deleteById(Long id);
    Admin selectById(Long id);
    List<Admin> selectAll();
    /**
     * 根据用户名查询管理员
     */
    Admin selectByUsername(String username);
}
