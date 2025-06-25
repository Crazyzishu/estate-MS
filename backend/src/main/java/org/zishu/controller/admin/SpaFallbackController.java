package org.zishu.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class SpaFallbackController {

    @RequestMapping({"/dashboard", "/houses", "/clients", "/usermanager", "/contracts", "/complaint"})
    public String redirect(HttpServletRequest request) {
        // 打印调试日志（可选）
        // System.out.println("Redirecting SPA route: " + request.getRequestURI());

        return "forward:/";
    }
}
