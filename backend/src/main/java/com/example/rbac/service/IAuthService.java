package com.example.rbac.service;

import com.example.rbac.dto.LoginRequest;
import com.example.rbac.dto.UserInfoVO;

/**
 * 登录服务接口
 */
public interface IAuthService {

    /**
     * 登录
     */
    String login(LoginRequest request);

    /**
     * 登出
     */
    void logout();

    /**
     * 获取当前登录用户信息
     */
    UserInfoVO getUserInfo();
}
