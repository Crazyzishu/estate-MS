package org.zishu.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.zishu.pojo.Admin;

@Mapper
public interface RegisterMapper {
    boolean existsByUsername(@Param("username") String username);
    void insertAdmin(Admin admin);
}