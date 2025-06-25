package org.zishu.service.admin.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zishu.mapper.AdminMapper;
import org.zishu.pojo.Admin;
import org.zishu.service.admin.LoginService;

@Service
@Slf4j
public class LoginServiceImpl implements LoginService {

    @Autowired
    private AdminMapper adminMapper;

    @Override
    public boolean authenticate(String username, String password) {
        log.info("尝试登录: {}", username);
        Admin admin = adminMapper.selectByUsername(username);
        return admin != null && admin.getPassword().equals(password);
    }
}