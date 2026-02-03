package com.example.rbac.utils;

import cn.hutool.core.util.StrUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Security工具类
 */
public class SecurityUtils {

    /**
     * 获取Authentication
     */
    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * 获取用户名
     */
    public static String getUsername() {
        try {
            Authentication authentication = getAuthentication();
            if (authentication == null) {
                return "system";
            }
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetails) {
                return ((UserDetails) principal).getUsername();
            }
            if (principal instanceof String) {
                return (String) principal;
            }
            return "system";
        } catch (Exception e) {
            return "system";
        }
    }

    /**
     * 获取用户ID
     */
    public static Long getUserId() {
        try {
            String username = getUsername();
            if (StrUtil.isBlank(username) || "system".equals(username)) {
                return null;
            }
            // 从UserDetails中获取用户ID(需要自定义UserDetails)
            Authentication authentication = getAuthentication();
            if (authentication != null && authentication.getPrincipal() instanceof LoginUser) {
                return ((LoginUser) authentication.getPrincipal()).getUserId();
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }
}
