package org.zishu.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.zishu.util.TokenUtils;

@Component
public class TokenInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 白名单路径（无需 Token）
        String uri = request.getRequestURI();
        if (isWhiteList(uri)) {
            return true;
        }

        // 获取请求头中的 Token
        String token = request.getHeader("Authorization");

        if (token == null || !TokenUtils.validateToken(token)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("未授权访问");
            return false;
        }

        return true;
    }

    // 判断是否属于白名单
    private boolean isWhiteList(String uri) {
        return uri.equals("/api/admin/login");
    }
}