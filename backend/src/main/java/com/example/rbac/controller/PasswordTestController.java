package com.example.rbac.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 密码测试控制器（仅用于调试）
 */
@RestController
@RequestMapping("/test")
public class PasswordTestController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/password")
    public Map<String, Object> testPassword(
            @RequestParam String raw,
            @RequestParam String encoded) {
        Map<String, Object> result = new HashMap<>();
        result.put("rawPassword", raw);
        result.put("encodedPassword", encoded);
        result.put("matches", passwordEncoder.matches(raw, encoded));
        result.put("newEncoded", passwordEncoder.encode(raw));
        return result;
    }
}
