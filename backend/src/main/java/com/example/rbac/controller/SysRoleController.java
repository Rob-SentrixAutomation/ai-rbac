package com.example.rbac.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.rbac.common.Result;
import com.example.rbac.domain.SysRole;
import com.example.rbac.dto.RoleAddDTO;
import com.example.rbac.dto.RoleQueryDTO;
import com.example.rbac.dto.RoleUpdateDTO;
import com.example.rbac.dto.RoleVO;
import com.example.rbac.service.ISysRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 角色管理控制器
 */
@Api(tags = "角色管理")
@RestController
@RequestMapping("/system/role")
public class SysRoleController {

    @Autowired
    private ISysRoleService roleService;

    @ApiOperation("查询角色列表")
    @PreAuthorize("@pms.hasPerm('system:role:list')")
    @GetMapping("/list")

    public Result<Page<RoleVO>> list(
            @ApiParam("页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @ApiParam("每页数量") @RequestParam(defaultValue = "10") Integer pageSize,
            RoleQueryDTO queryDTO) {
        
        Page<SysRole> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(queryDTO.getRoleName() != null, SysRole::getRoleName, queryDTO.getRoleName())
                .like(queryDTO.getRoleKey() != null, SysRole::getRoleKey, queryDTO.getRoleKey())
                .eq(queryDTO.getStatus() != null, SysRole::getStatus, queryDTO.getStatus())
                .orderByAsc(SysRole::getRoleSort)
                .orderByDesc(SysRole::getCreateTime);
        
        Page<SysRole> result = roleService.page(page, wrapper);
        
        // 转换为VO
        Page<RoleVO> voPage = new Page<>(result.getCurrent(), result.getSize(), result.getTotal());
        voPage.setRecords(result.getRecords().stream().map(role -> {
            RoleVO vo = new RoleVO();
            BeanUtils.copyProperties(role, vo);
            return vo;
        }).collect(java.util.stream.Collectors.toList()));
        
        return Result.success(voPage);
    }

    @ApiOperation("根据ID查询角色")
    @PreAuthorize("@pms.hasPerm('system:role:query')")
    @GetMapping("/{roleId}")

    public Result<RoleVO> getById(@ApiParam("角色ID") @PathVariable Long roleId) {
        SysRole role = roleService.getById(roleId);
        RoleVO vo = new RoleVO();
        BeanUtils.copyProperties(role, vo);
        
        // 查询角色关联的菜单ID列表
        List<Long> menuIds = roleService.getMenuIdsByRoleId(roleId);
        vo.setMenuIds(menuIds);
        
        return Result.success(vo);
    }


    @ApiOperation("新增角色")
    @PreAuthorize("@pms.hasPerm('system:role:add')")
    @PostMapping

    public Result<Void> add(@Validated @RequestBody RoleAddDTO dto) {
        // DTO转Entity
        SysRole role = new SysRole();
        BeanUtils.copyProperties(dto, role);
        
        roleService.save(role);
        
        // 分配菜单权限
        if (dto.getMenuIds() != null && !dto.getMenuIds().isEmpty()) {
            roleService.assignMenus(role.getRoleId(), dto.getMenuIds());
        }
        
        return Result.success();
    }


    @ApiOperation("修改角色")
    @PreAuthorize("@pms.hasPerm('system:role:edit')")
    @PutMapping

    public Result<Void> update(@Validated @RequestBody RoleUpdateDTO dto) {
        // DTO转Entity
        SysRole role = new SysRole();
        BeanUtils.copyProperties(dto, role);
        
        roleService.updateById(role);
        
        // 分配菜单权限
        if (dto.getMenuIds() != null) {
            roleService.assignMenus(role.getRoleId(), dto.getMenuIds());
        }
        
        return Result.success();
    }


    @ApiOperation("删除角色")
    @PreAuthorize("@pms.hasPerm('system:role:remove')")
    @DeleteMapping("/{roleId}")

    public Result<Void> remove(@ApiParam("角色ID") @PathVariable Long roleId) {
        roleService.removeById(roleId);
        return Result.success();
    }

    @ApiOperation("分配数据权限")
    @PreAuthorize("@pms.hasPerm('system:role:edit')")
    @PutMapping("/dataScope")

    public Result<Void> dataScope(@RequestBody SysRole role) {
        // 只更新数据权限字段
        SysRole updateRole = new SysRole();
        updateRole.setRoleId(role.getRoleId());
        updateRole.setDataScope(role.getDataScope());
        roleService.updateById(updateRole);
        return Result.success();
    }
}
