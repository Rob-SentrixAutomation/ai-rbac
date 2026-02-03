package com.example.rbac.controller;

import com.example.rbac.common.Result;
import com.example.rbac.domain.SysUser;
import com.example.rbac.service.ISysUserService;
import com.example.rbac.utils.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 个人中心
 */
@Api(tags = "个人中心")
@RestController
@RequestMapping("/system/user/profile")
public class ProfileController {

    @Autowired
    private ISysUserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @ApiOperation("更新个人信息")
    @PutMapping
    public Result<Void> updateProfile(@Validated @RequestBody SysUser user) {
        Long userId = SecurityUtils.getUserId();
        if (userId == null) {
            return Result.error("未登录");
        }
        user.setUserId(userId);
        // 不允许前端修改状态等敏感字段
        userService.updateById(user);
        return Result.success();
    }

    @ApiOperation("修改密码")
    @PutMapping("/password")
    public Result<Void> updatePassword(@RequestParam String oldPwd, @RequestParam String newPwd) {
        Long userId = SecurityUtils.getUserId();
        if (userId == null) {
            return Result.error("未登录");
        }
        SysUser dbUser = userService.getById(userId);
        if (dbUser == null) {
            return Result.error("用户不存在");
        }
        if (!passwordEncoder.matches(oldPwd, dbUser.getPassword())) {
            return Result.error("旧密码不正确");
        }
        SysUser updateUser = new SysUser();
        updateUser.setUserId(userId);
        updateUser.setPassword(passwordEncoder.encode(newPwd));
        userService.updateById(updateUser);
        return Result.success();
    }
}
