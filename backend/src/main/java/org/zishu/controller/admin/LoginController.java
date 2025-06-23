package org.zishu.controller.admin;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.zishu.pojo.Admin;
import org.zishu.pojo.LoginRequest;
import org.zishu.pojo.Result;
import org.zishu.service.admin.AdminService;
import org.zishu.util.TokenUtils;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/login")
@Slf4j
public class LoginController {

    @Autowired
    private AdminService adminService;

    /**
     * 管理员登录接口
     */
    @PostMapping
    public Result login(@RequestBody LoginRequest request) {
        log.info("管理员登录: {}", request.getUsername());

        // 调用 service 验证用户名密码
        Admin admin = adminService.login(request.getUsername(), request.getPassword());
        if (admin == null) {
            return Result.error("用户名或密码错误");
        }

        // 生成 Token
        String token = TokenUtils.generateToken();

        // 构建返回数据
        Map<String, Object> data = new HashMap<>();
        data.put("token", token);
        data.put("adminId", admin.getAdminId());
        data.put("realName", admin.getRealName());

        return Result.success(data);
    }
}
