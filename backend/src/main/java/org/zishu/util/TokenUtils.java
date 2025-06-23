package org.zishu.util;

import java.util.UUID;

public class TokenUtils {

    // 生成 Token（模拟）
    public static String generateToken() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    // 校验 Token（模拟）
    public static boolean validateToken(String token) {
        // 后续可替换为 Redis 或 JWT 校验逻辑
        return token != null && !token.isEmpty();
    }
}
