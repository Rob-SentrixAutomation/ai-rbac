package com.example.rbac.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.rbac.domain.SysUser;
import com.example.rbac.mapper.SysUserMapper;
import com.example.rbac.utils.LoginUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

/**
 * 用户详情服务
 */
@Slf4j
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("开始加载用户信息: {}", username);
        
        // 查询用户
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getUsername, username);
        SysUser user = sysUserMapper.selectOne(wrapper);

        if (user == null) {
            log.error("用户不存在: {}", username);
            throw new UsernameNotFoundException("用户不存在");
        }

        log.info("找到用户: userId={}, username={}, status={}", user.getUserId(), user.getUsername(), user.getStatus());
        log.info("数据库密码前缀: {}", user.getPassword().substring(0, Math.min(20, user.getPassword().length())));

        if (user.getStatus() == 1) {
            log.error("用户已停用: {}", username);
            throw new UsernameNotFoundException("用户已停用");
        }

        // 查询用户权限
        List<String> permissions = sysUserMapper.selectPermissionsByUsername(username);
        log.info("用户权限数量: {}", permissions.size());
        
        // 查询数据权限范围
        String dataScope = sysUserMapper.selectDataScopeByUsername(username);
        if (StrUtil.isBlank(dataScope)) {
            dataScope = "5"; // 默认仅本人数据权限
        }
        log.info("用户数据权限范围: {}", dataScope);

        return new LoginUser(
                user.getUserId(),
                user.getDeptId(),
                user.getUsername(),
                user.getPassword(),
                new HashSet<>(permissions),
                dataScope
        );
    }
}
