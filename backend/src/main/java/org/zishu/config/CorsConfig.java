//package org.zishu.config;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.CorsRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//@Configuration
//public class CorsConfig implements WebMvcConfigurer {
//
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**") // 匹配所有以 / 开头的请求路径
//                .allowedOrigins("http://localhost:5175") // 允许来自前端的源
//                .allowedMethods("GET", "POST", "PUT", "DELETE") // 允许的方法
//                .allowedHeaders("*") // 允许所有请求头
//                .exposedHeaders("*") // 暴露所有响应头
//                .allowCredentials(true); // 是否允许发送 Cookie
//    }
//}