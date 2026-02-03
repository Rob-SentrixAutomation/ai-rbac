package com.example.rbac.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.rbac.domain.SysDept;

import java.util.List;

/**
 * 部门服务接口
 */
public interface ISysDeptService extends IService<SysDept> {
    List<SysDept> buildDeptTree(List<SysDept> depts);
}

