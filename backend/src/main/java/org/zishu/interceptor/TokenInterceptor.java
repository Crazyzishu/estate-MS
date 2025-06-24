package org.zishu.interceptor;

import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.zishu.util.TokenUtils;

@Component
//@WebFilter(urlPatterns = "/*")
public class TokenInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        // 白名单路径（无需 Token）
//        String uri = request.getRequestURI();
//        if (isWhiteList(uri)) {
//            return true;
//        }
//
//        // 获取请求头中的 Token
//        String token = request.getHeader("Authorization");
//        // 移除Bearer前缀
//        if (token != null && token.startsWith("Bearer ")) {
//            token = token.substring(7);
//        }
//
//        if (token == null || !TokenUtils.validateToken(token)) {
//            // 对于API请求返回401，对于页面请求重定向到登录页
//            if (uri.startsWith("/api/")) {
//                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//                response.getWriter().write("未授权访问");
//            } else {
//                response.sendRedirect("/login");
//            }
//            return false;
//        }

        return true;
    }

    // 判断是否属于白名单
    private boolean isWhiteList(String uri) {
        // 允许登录接口和静态资源请求
        return uri.startsWith("/api/login") || uri.startsWith("/api/register") || uri.startsWith("/static/") || uri.startsWith("/assets/");
    }
}