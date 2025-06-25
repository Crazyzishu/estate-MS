package org.zishu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zishu.pojo.Admin;
import org.zishu.pojo.Result;
import org.zishu.service.admin.RegisterService;

@RestController
@RequestMapping("/api")
public class RegisterController {

    @Autowired
    private RegisterService registerService;

    @PostMapping("/register")
    public Result register(@RequestBody Admin admin) {
        boolean isRegistered = registerService.register(admin);
        if (isRegistered) {
            return Result.success("注册成功");
        } else {
            return Result.error("用户名重复");
        }
    }
}