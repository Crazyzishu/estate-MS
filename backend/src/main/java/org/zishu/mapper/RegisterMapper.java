package org.zishu.mapper;

import org.apache.ibatis.annotations.Param;
import org.zishu.pojo.Admin;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RegisterMapper {

    void insertAdmin(@Param("admin") Admin admin);

    Admin findByUsername(@Param("username") String username);
}