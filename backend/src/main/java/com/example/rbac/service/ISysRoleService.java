package com.example.rbac.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.rbac.domain.SysRole;

import java.util.List;

/**
 * 角色服务接口
 */
public interface ISysRoleService extends IService<SysRole> {
    List<Long> getMenuIdsByRoleId(Long roleId);
    
    void assignMenus(Long roleId, List<Long> menuIds);
}

