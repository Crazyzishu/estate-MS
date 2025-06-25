package org.zishu.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.zishu.pojo.Admin;

@Mapper
public interface LoginMapper {
    Admin findByUsername(@Param("username") String username);
}