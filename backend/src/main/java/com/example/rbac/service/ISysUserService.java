package com.example.rbac.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.rbac.domain.SysUser;

/**
 * 用户服务接口
 */
public interface ISysUserService extends IService<SysUser> {

    java.util.List<String> getRoleKeysByUsername(String username);

    java.util.List<Long> getRoleIdsByUserId(Long userId);

    void assignRoles(Long userId, java.util.List<Long> roleIds);
}


