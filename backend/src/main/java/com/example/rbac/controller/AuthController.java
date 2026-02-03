package com.example.rbac.controller;

import com.example.rbac.common.Result;
import com.example.rbac.dto.LoginRequest;
import com.example.rbac.dto.LoginResponse;
import com.example.rbac.dto.UserInfoVO;
import com.example.rbac.service.IAuthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 认证控制器
 */
@Api(tags = "认证管理")
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private IAuthService authService;

    @ApiOperation("用户登录")
    @PostMapping("/login")
    public Result<LoginResponse> login(@Validated @RequestBody LoginRequest request) {
        String token = authService.login(request);
        return Result.success("登录成功", new LoginResponse(token));
    }

    @ApiOperation("获取当前用户信息")
    @GetMapping("/info")
    public Result<UserInfoVO> info() {
        UserInfoVO userInfo = authService.getUserInfo();
        return Result.success(userInfo);
    }

    @ApiOperation("获取个人信息")
    @GetMapping("/profile")
    public Result<UserInfoVO> profile() {
        UserInfoVO userInfo = authService.getUserInfo();
        return Result.success(userInfo);
    }


    @ApiOperation("用户登出")
    @PostMapping("/logout")
    public Result<Void> logout() {
        authService.logout();
        return Result.success("登出成功", null);
    }
}
