package org.zishu.controller.admin;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.zishu.pojo.Admin;
import org.zishu.pojo.Result;
import org.zishu.service.admin.AdminService;

import java.util.List;

@RestController
@RequestMapping("/api/admins")
@Slf4j
public class AdminController {

    @Autowired
    private AdminService adminService;

    /**
     * 查询所有管理员
     */
    @GetMapping
    public Result getAllAdmins() {
        List<Admin> admins = adminService.getAllAdmins();
        return Result.success(admins);
    }

    /**
     * 根据 ID 查询管理员
     */
    @GetMapping("/{id}")
    public Result getAdminById(@PathVariable Long id) {
        Admin admin = adminService.getAdminById(id);
        return Result.success(admin);
    }

    /**
     * 更新管理员信息
     */
    @PutMapping("/{id}")
    public Result updateAdmin(@PathVariable Long id, @RequestBody Admin admin) {
        admin.setAdminId(id);
        adminService.updateAdmin(admin);
        return Result.success();
    }

    /**
     * 删除管理员
     */
    @DeleteMapping("/{id}")
    public Result deleteAdmin(@PathVariable Long id) {
        adminService.deleteAdmin(id);
        return Result.success();
    }
}
