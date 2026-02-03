package com.example.rbac.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.rbac.annotation.DataScope;
import com.example.rbac.common.Result;
import com.example.rbac.domain.SysUser;
import com.example.rbac.dto.UserAddDTO;
import com.example.rbac.dto.UserQueryDTO;
import com.example.rbac.dto.UserUpdateDTO;
import com.example.rbac.dto.UserVO;
import com.example.rbac.service.ISysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 用户管理控制器
 */
@Api(tags = "用户管理")
@RestController
@RequestMapping("/system/user")
public class SysUserController {

    @Autowired
    private ISysUserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @ApiOperation("查询用户列表")
    @PreAuthorize("@pms.hasPerm('system:user:list')")
    @GetMapping("/list")

    @DataScope(deptAlias = "d", userAlias = "u")
    public Result<Page<UserVO>> list(
            @ApiParam("页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @ApiParam("每页数量") @RequestParam(defaultValue = "10") Integer pageSize,
            UserQueryDTO queryDTO) {
        
        Page<SysUser> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(queryDTO.getUsername() != null, SysUser::getUsername, queryDTO.getUsername())
                .like(queryDTO.getNickname() != null, SysUser::getNickname, queryDTO.getNickname())
                .like(queryDTO.getPhone() != null, SysUser::getPhone, queryDTO.getPhone())
                .eq(queryDTO.getStatus() != null, SysUser::getStatus, queryDTO.getStatus())
                .eq(queryDTO.getDeptId() != null, SysUser::getDeptId, queryDTO.getDeptId())
                .orderByDesc(SysUser::getCreateTime);
        
        Page<SysUser> result = userService.page(page, wrapper);
        
        // 转换为VO
        Page<UserVO> voPage = new Page<>(result.getCurrent(), result.getSize(), result.getTotal());
        voPage.setRecords(result.getRecords().stream().map(user -> {
            UserVO vo = new UserVO();
            BeanUtils.copyProperties(user, vo);
            // 角色
            vo.setRoles(userService.getRoleKeysByUsername(user.getUsername()));
            vo.setRoleIds(userService.getRoleIdsByUserId(user.getUserId()));
            return vo;
        }).collect(java.util.stream.Collectors.toList()));
        
        return Result.success(voPage);
    }

    @ApiOperation("根据ID查询用户")
    @PreAuthorize("@pms.hasPerm('system:user:query')")
    @GetMapping("/{userId}")

    public Result<UserVO> getById(@ApiParam("用户ID") @PathVariable Long userId) {
        SysUser user = userService.getById(userId);
        UserVO vo = new UserVO();
        BeanUtils.copyProperties(user, vo);
        vo.setRoleIds(userService.getRoleIdsByUserId(userId));
        vo.setRoles(userService.getRoleKeysByUsername(user.getUsername()));
        return Result.success(vo);
    }

    @ApiOperation("新增用户")
    @PreAuthorize("@pms.hasPerm('system:user:add')")
    @PostMapping

    public Result<Void> add(@Validated @RequestBody UserAddDTO dto) {
        // DTO转Entity
        SysUser user = new SysUser();
        BeanUtils.copyProperties(dto, user);
        
        // 加密密码
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        
        userService.save(user);
        // 角色分配
        userService.assignRoles(user.getUserId(), dto.getRoleIds());
        return Result.success();
    }

    @ApiOperation("修改用户")
    @PreAuthorize("@pms.hasPerm('system:user:edit')")
    @PutMapping

    public Result<Void> update(@Validated @RequestBody UserUpdateDTO dto) {
        // DTO转Entity
        SysUser user = new SysUser();
        BeanUtils.copyProperties(dto, user);
        

        userService.updateById(user);
        // 更新角色
        userService.assignRoles(user.getUserId(), dto.getRoleIds());
        return Result.success();
    }


    @ApiOperation("删除用户")
    @PreAuthorize("@pms.hasPerm('system:user:remove')")
    @DeleteMapping("/{userId}")

    public Result<Void> remove(@ApiParam("用户ID") @PathVariable Long userId) {
        userService.removeById(userId);
        return Result.success();
    }

    @ApiOperation("重置密码")
    @PreAuthorize("@pms.hasPerm('system:user:edit')")
    @PutMapping("/resetPassword")

    public Result<Void> resetPassword(@RequestBody SysUser user) {
        // 只更新密码字段
        SysUser updateUser = new SysUser();
        updateUser.setUserId(user.getUserId());
        updateUser.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.updateById(updateUser);
        return Result.success();
    }
}
