package org.zishu.service.admin.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zishu.mapper.AdminMapper;
import org.zishu.pojo.Admin;
import org.zishu.service.admin.AdminService;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminMapper adminMapper;

    @Override
    public void register(Admin admin) {
        // 检查用户名是否已存在
        Admin existingAdmin = adminMapper.selectByUsername(admin.getUsername());
        if (existingAdmin != null) {
            throw new RuntimeException("用户名已存在");
        }
        // 设置默认值
        admin.setRole("ADMIN"); // 默认角色
        admin.setIsActive(true); // 默认激活
        adminMapper.insert(admin);
    }

    @Override
    public List<Admin> getAllAdmins() {
        return adminMapper.selectAll();
    }

    @Override
    public Admin getAdminById(Long id) {
        return adminMapper.selectById(id);
    }

    @Override
    public void updateAdmin(Admin admin) {
        adminMapper.update(admin);
    }

    @Override
    public void deleteAdmin(Long id) {
        adminMapper.deleteById(id);
    }

    @Override
    public Admin login(String username, String password) {
        Admin admin = adminMapper.selectByUsername(username);
        if (admin != null && admin.getPassword().equals(password)) {
            return admin;
        }
        return null;
    }
}