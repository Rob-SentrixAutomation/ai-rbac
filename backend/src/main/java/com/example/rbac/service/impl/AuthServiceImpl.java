package com.example.rbac.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.rbac.config.RedisProperties;
import com.example.rbac.domain.SysUser;
import com.example.rbac.dto.LoginRequest;
import com.example.rbac.dto.UserInfoVO;
import com.example.rbac.exception.BusinessException;
import com.example.rbac.mapper.SysUserMapper;
import com.example.rbac.service.IAuthService;
import com.example.rbac.utils.JwtUtils;
import com.example.rbac.utils.LoginUser;
import com.example.rbac.utils.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 登录服务实现
 */
@Slf4j
@Service
public class AuthServiceImpl implements IAuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private RedisProperties redisProperties;

    private static final String LOGIN_USER_KEY = "login:user:";

    @Override
    public String login(LoginRequest request) {
        // 用户认证
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
        } catch (Exception e) {
            log.error("用户登录失败: {}", request.getUsername(), e);
            throw new BusinessException("用户名或密码错误");
        }

        LoginUser loginUser = (LoginUser) authentication.getPrincipal();

        // 生成token
        String token = jwtUtils.generateToken(loginUser.getUsername());

        // 只存储用户名到Redis，标记token有效性
        String key = LOGIN_USER_KEY + loginUser.getUsername();
        redisTemplate.opsForValue().set(key, loginUser.getUsername(), 
            redisProperties.getTokenExpireMinutes(), TimeUnit.MINUTES);

        log.info("用户登录成功: {}", loginUser.getUsername());
        return token;
    }

    @Override
    public void logout() {
        String username = SecurityUtils.getUsername();
        String key = LOGIN_USER_KEY + username;
        redisTemplate.delete(key);
        log.info("用户登出: {}", username);
    }

    @Override
    public UserInfoVO getUserInfo() {
        String username = SecurityUtils.getUsername();
        
        // 查询用户基本信息
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getUsername, username);
        SysUser user = sysUserMapper.selectOne(wrapper);
        
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        
        // 查询角色列表
        List<String> roles = sysUserMapper.selectRoleKeysByUsername(username);
        if (roles == null || roles.isEmpty()) {
            roles = new ArrayList<>();
            roles.add("user"); // 默认角色
        }
        
        // 查询权限列表
        List<String> permissions = sysUserMapper.selectPermissionsByUsername(username);
        if (permissions == null) {
            permissions = new ArrayList<>();
        }
        
        // 组装返回对象
        UserInfoVO userInfo = new UserInfoVO();
        userInfo.setUserId(user.getUserId());
        userInfo.setUsername(user.getUsername());
        userInfo.setNickname(user.getNickname());
        userInfo.setAvatar(user.getAvatar());
        userInfo.setRoles(roles);
        userInfo.setPermissions(permissions);
        
        return userInfo;
    }
}
