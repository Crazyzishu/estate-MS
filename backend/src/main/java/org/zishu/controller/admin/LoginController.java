package org.zishu.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zishu.pojo.Admin;
import org.zishu.pojo.Result;
import org.zishu.service.admin.LoginService;

@RestController
@RequestMapping("/api")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @PostMapping("/login")
    public Result login(@RequestBody Admin admin) {
        boolean isAuthenticated = loginService.authenticate(admin.getUsername(), admin.getPassword());
        if (isAuthenticated) {
            return Result.success("登录成功");
        } else {
            return Result.error("用户名或密码错误");
        }
    }
}