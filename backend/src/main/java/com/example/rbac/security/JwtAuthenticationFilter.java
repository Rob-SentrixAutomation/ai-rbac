package com.example.rbac.security;

import cn.hutool.core.util.StrUtil;
import com.example.rbac.config.RedisProperties;
import com.example.rbac.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * JWT认证过滤器
 */
@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private RedisProperties redisProperties;

    private static final String TOKEN_PREFIX = "Bearer ";
    private static final String LOGIN_USER_KEY = "login:user:";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        String token = getTokenFromRequest(request);
        log.debug("JWT过滤器 - 请求路径: {}, Token: {}", request.getRequestURI(), token != null ? "存在" : "不存在");
        
        if (StrUtil.isNotBlank(token)) {
            boolean isValid = jwtUtils.validateToken(token);
            log.debug("Token验证结果: {}", isValid);
            
            if (isValid) {
                String username = jwtUtils.getUsernameFromToken(token);
                log.debug("从Token中解析用户名: {}", username);
                
                // 检查Redis中是否存在该用户的登录标记
                String key = LOGIN_USER_KEY + username;
                String cachedUsername = (String) redisTemplate.opsForValue().get(key);
                
                if (cachedUsername != null) {
                    // 刷新token过期时间
                    redisTemplate.expire(key, redisProperties.getTokenExpireMinutes(), TimeUnit.MINUTES);
                    
                    // 从数据库加载用户详细信息和权限
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                    
                    // 设置认证信息
                    UsernamePasswordAuthenticationToken authentication = 
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    log.debug("认证信息设置成功: {}", username);
                } else {
                    log.warn("Redis中未找到用户登录标记: {}", username);
                }
            } else {
                log.warn("Token验证失败");
            }
        }
        
        filterChain.doFilter(request, response);
    }

    /**
     * 从请求中获取Token
     */
    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StrUtil.isNotBlank(bearerToken) && bearerToken.startsWith(TOKEN_PREFIX)) {
            return bearerToken.substring(TOKEN_PREFIX.length());
        }
        return null;
    }
}
