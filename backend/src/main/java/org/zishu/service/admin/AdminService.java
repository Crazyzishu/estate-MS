package org.zishu.service.admin;

import org.zishu.pojo.Admin;

import java.util.List;

public interface AdminService {
    List<Admin> getAllAdmins();
    Admin getAdminById(Long id);
    void updateAdmin(Admin admin);
    void deleteAdmin(Long id);
    /**
     * 登录验证方法
     * @param username 用户名
     * @param password 密码
     * @return 匹配的管理员对象，若无匹配则返回 null
     */
    Admin login(String username, String password);
    // 添加注册方法
    void register(Admin admin);
}