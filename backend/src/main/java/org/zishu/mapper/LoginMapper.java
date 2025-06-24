package org.zishu.mapper;

import org.zishu.pojo.Admin;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface LoginMapper {

    Admin findByUsername(@Param("username") String username);
}