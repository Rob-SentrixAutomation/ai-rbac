package com.example.rbac.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.rbac.common.Result;
import com.example.rbac.domain.SysDept;
import com.example.rbac.service.ISysDeptService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 部门管理控制器
 */
@Api(tags = "部门管理")
@RestController
@RequestMapping("/system/dept")
public class SysDeptController {

    @Autowired
    private ISysDeptService deptService;

    @ApiOperation("查询部门列表")
    @PreAuthorize("@pms.hasPerm('system:dept:list')")
    @GetMapping("/list")

    public Result<List<SysDept>> list(
            @ApiParam("部门名称") @RequestParam(required = false) String deptName) {
        
        LambdaQueryWrapper<SysDept> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(deptName != null, SysDept::getDeptName, deptName)
                .orderByAsc(SysDept::getParentId)
                .orderByAsc(SysDept::getOrderNum);
        
        List<SysDept> list = deptService.list(wrapper);
        return Result.success(list);
    }

    @ApiOperation("查询部门树")
    @PreAuthorize("@pms.hasPerm('system:dept:list')")
    @GetMapping("/tree")

    public Result<List<SysDept>> tree() {
        LambdaQueryWrapper<SysDept> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(SysDept::getParentId)
                .orderByAsc(SysDept::getOrderNum);

        List<SysDept> list = deptService.list(wrapper);
        return Result.success(deptService.buildDeptTree(list));
    }


    @ApiOperation("根据ID查询部门")
    @PreAuthorize("@pms.hasPerm('system:dept:query')")
    @GetMapping("/{deptId}")

    public Result<SysDept> getById(@ApiParam("部门ID") @PathVariable Long deptId) {
        SysDept dept = deptService.getById(deptId);
        return Result.success(dept);
    }

    @ApiOperation("新增部门")
    @PreAuthorize("@pms.hasPerm('system:dept:add')")
    @PostMapping

    public Result<Void> add(@RequestBody SysDept dept) {
        deptService.save(dept);
        return Result.success();
    }

    @ApiOperation("修改部门")
    @PreAuthorize("@pms.hasPerm('system:dept:edit')")
    @PutMapping

    public Result<Void> update(@RequestBody SysDept dept) {
        deptService.updateById(dept);
        return Result.success();
    }

    @ApiOperation("删除部门")
    @PreAuthorize("@pms.hasPerm('system:dept:remove')")
    @DeleteMapping("/{deptId}")

    public Result<Void> remove(@ApiParam("部门ID") @PathVariable Long deptId) {
        deptService.removeById(deptId);
        return Result.success();
    }
}
