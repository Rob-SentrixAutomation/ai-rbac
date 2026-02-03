package com.example.rbac.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * JWT配置属性
 */
@Data
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {
    
    /**
     * JWT密钥
     */
    private String secret;
    
    /**
     * JWT过期时间(毫秒)
     */
    private Long expiration;
    
    /**
     * JWT请求头
     */
    private String header;
    
    /**
     * JWT token前缀
     */
    private String prefix;
}
