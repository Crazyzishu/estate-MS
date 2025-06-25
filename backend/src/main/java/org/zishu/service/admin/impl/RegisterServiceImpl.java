package org.zishu.service.admin.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zishu.mapper.RegisterMapper;
import org.zishu.pojo.Admin;
import org.zishu.service.admin.RegisterService;

@Service
public class RegisterServiceImpl implements RegisterService {

    @Autowired
    private RegisterMapper registerMapper;

    @Override
    public boolean register(Admin admin) {
        if (registerMapper.existsByUsername(admin.getUsername())) {
            return false;
        }
        registerMapper.insertAdmin(admin);
        return true;
    }
}