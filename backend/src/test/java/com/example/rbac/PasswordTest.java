package com.example.rbac;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 密码测试工具
 */
public class PasswordTest {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        
        // 测试密码：admin123
        String rawPassword = "admin123";
        String encodedPassword = encoder.encode(rawPassword);
        
        System.out.println("原始密码: " + rawPassword);
        System.out.println("加密后密码: " + encodedPassword);
        System.out.println();
        
        // 验证数据库中的密码
        String dbPassword = "$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE/TU.qYCrm/NW";
        boolean matches = encoder.matches(rawPassword, dbPassword);
        System.out.println("数据库密码: " + dbPassword);
        System.out.println("密码验证结果: " + matches);
        
        // 生成新密码
        System.out.println("\n=== 生成新的加密密码 ===");
        System.out.println("admin123 -> " + encoder.encode("admin123"));
        System.out.println("123456 -> " + encoder.encode("123456"));
    }
}
