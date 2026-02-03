package com.example.rbac.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.rbac.common.Result;
import com.example.rbac.domain.SysMenu;
import com.example.rbac.service.ISysMenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 菜单管理控制器
 */
@Api(tags = "菜单管理")
@RestController
@RequestMapping("/system/menu")
public class SysMenuController {

    @Autowired
    private ISysMenuService menuService;

    @ApiOperation("查询菜单列表")
    @PreAuthorize("@pms.hasPerm('system:menu:list')")
    @GetMapping("/list")

    public Result<List<SysMenu>> list(
            @ApiParam("菜单名称") @RequestParam(required = false) String menuName,
            @ApiParam("权限标识") @RequestParam(required = false) String perms) {
        
        LambdaQueryWrapper<SysMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(menuName != null, SysMenu::getMenuName, menuName)
                .like(perms != null, SysMenu::getPerms, perms)
                .orderByAsc(SysMenu::getParentId)
                .orderByAsc(SysMenu::getOrderNum);
        
        List<SysMenu> list = menuService.list(wrapper);
        return Result.success(list);
    }

    @ApiOperation("查询菜单树")
    @PreAuthorize("@pms.hasPerm('system:menu:list')")
    @GetMapping("/tree")

    public Result<List<SysMenu>> tree() {
        LambdaQueryWrapper<SysMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(SysMenu::getParentId)
                .orderByAsc(SysMenu::getOrderNum);

        List<SysMenu> list = menuService.list(wrapper);
        return Result.success(menuService.buildMenuTree(list));
    }


    @ApiOperation("根据ID查询菜单")
    @PreAuthorize("@pms.hasPerm('system:menu:query')")
    @GetMapping("/{menuId}")

    public Result<SysMenu> getById(@ApiParam("菜单ID") @PathVariable Long menuId) {
        SysMenu menu = menuService.getById(menuId);
        return Result.success(menu);
    }

    @ApiOperation("新增菜单")
    @PreAuthorize("@pms.hasPerm('system:menu:add')")
    @PostMapping

    public Result<Void> add(@RequestBody SysMenu menu) {
        menuService.save(menu);
        return Result.success();
    }

    @ApiOperation("修改菜单")
    @PreAuthorize("@pms.hasPerm('system:menu:edit')")
    @PutMapping

    public Result<Void> update(@RequestBody SysMenu menu) {
        menuService.updateById(menu);
        return Result.success();
    }

    @ApiOperation("删除菜单")
    @PreAuthorize("@pms.hasPerm('system:menu:remove')")
    @DeleteMapping("/{menuId}")

    public Result<Void> remove(@ApiParam("菜单ID") @PathVariable Long menuId) {
        menuService.removeById(menuId);
        return Result.success();
    }
}
