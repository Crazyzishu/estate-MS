package org.zishu.service.admin.impl;

import org.zishu.pojo.Admin;
import org.zishu.mapper.LoginMapper;
import org.zishu.service.admin.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private LoginMapper loginMapper;

    @Override
    public boolean authenticate(String username, String password) {
        Admin admin = loginMapper.findByUsername(username);
        return admin != null && admin.getPassword().equals(password);
    }
}