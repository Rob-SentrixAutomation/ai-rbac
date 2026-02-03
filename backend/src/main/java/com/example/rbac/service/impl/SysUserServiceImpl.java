package com.example.rbac.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.rbac.domain.SysUser;
import com.example.rbac.mapper.SysUserMapper;
import com.example.rbac.service.ISysUserService;
import org.springframework.stereotype.Service;

/**
 * 用户服务实现
 */
@Service
@org.springframework.transaction.annotation.Transactional
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {

    @org.springframework.beans.factory.annotation.Autowired
    private com.example.rbac.mapper.SysUserRoleMapper userRoleMapper;

    @Override
    public java.util.List<String> getRoleKeysByUsername(String username) {
        return this.baseMapper.selectRoleKeysByUsername(username);
    }

    @Override
    public java.util.List<Long> getRoleIdsByUserId(Long userId) {
        return this.baseMapper.selectRoleIdsByUserId(userId);
    }

    @Override
    public void assignRoles(Long userId, java.util.List<Long> roleIds) {
        // 清理旧关联
        com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<com.example.rbac.domain.SysUserRole> wrapper =
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<>();
        wrapper.eq(com.example.rbac.domain.SysUserRole::getUserId, userId);
        userRoleMapper.delete(wrapper);

        if (roleIds != null && !roleIds.isEmpty()) {
            for (Long roleId : roleIds) {
                com.example.rbac.domain.SysUserRole ur = new com.example.rbac.domain.SysUserRole();
                ur.setUserId(userId);
                ur.setRoleId(roleId);
                userRoleMapper.insert(ur);
            }
        }
    }
}


