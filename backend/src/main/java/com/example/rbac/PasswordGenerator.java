package com.example.rbac;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordGenerator {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        
        String password = "admin123";
        String encoded = encoder.encode(password);
        
        System.out.println("原始密码: " + password);
        System.out.println("加密密码: " + encoded);
        System.out.println();
        
        // 验证
        boolean matches = encoder.matches(password, encoded);
        System.out.println("验证结果: " + matches);
        System.out.println();
        
        // 验证数据库中的密码
        String dbPassword = "$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE/TU.qYCrm/NW";
        boolean dbMatches = encoder.matches(password, dbPassword);
        System.out.println("数据库密码验证: " + dbMatches);
        
        if (!dbMatches) {
            System.out.println();
            System.out.println("请使用以下SQL更新密码:");
            System.out.println("UPDATE sys_user SET password = '" + encoded + "' WHERE username = 'admin';");
        }
    }
}
