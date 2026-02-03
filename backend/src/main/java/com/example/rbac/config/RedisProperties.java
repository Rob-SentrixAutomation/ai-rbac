package com.example.rbac.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Redis缓存配置属性
 */
@Data
@Component
@ConfigurationProperties(prefix = "spring.redis.cache")
public class RedisProperties {

    /**
     * 用户登录token缓存过期时间(分钟)
     */
    private Integer tokenExpireMinutes = 30;
}
