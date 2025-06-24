package org.zishu.util;

import java.util.UUID;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Date;

public class TokenUtils {
    // Token 过期时间: 2小时 (7200000毫秒)
    private static final long TOKEN_EXPIRE_TIME = 7200000;
    
    // 存储有效的token及其创建时间
    private static final Map<String, Long> tokenStore = new ConcurrentHashMap<>();

    /**
     * 生成Token并存储
     */
    public static String generateToken() {
        String token = UUID.randomUUID().toString().replace("-", "");
        // 存储token创建时间
        tokenStore.put(token, System.currentTimeMillis());
        return token;
    }

    /**
     * 校验Token是否有效
     */
    public static boolean validateToken(String token) {
        if (token == null || token.isEmpty()) {
            return false;
        }
        
        Long createTime = tokenStore.get(token);
        if (createTime == null) {
            return false;
        }
        
        // 检查token是否过期
        if (System.currentTimeMillis() - createTime > TOKEN_EXPIRE_TIME) {
            // 过期则移除
            tokenStore.remove(token);
            return false;
        }
        
        return true;
    }

    /**
     * 移除Token
     */
    public static void removeToken(String token) {
        if (token != null) {
            tokenStore.remove(token);
        }
    }
}