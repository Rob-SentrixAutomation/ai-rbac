package com.example.rbac.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 角色新增DTO
 */
@Data
@ApiModel("角色新增")
public class RoleAddDTO {

    @ApiModelProperty(value = "角色名称", required = true)
    @NotBlank(message = "角色名称不能为空")
    private String roleName;

    @ApiModelProperty(value = "角色权限字符串", required = true)
    @NotBlank(message = "角色权限字符串不能为空")
    private String roleKey;

    @ApiModelProperty(value = "显示顺序", required = true)
    @NotNull(message = "显示顺序不能为空")
    private Integer roleSort;

    @ApiModelProperty("数据范围(1全部数据权限 2自定数据权限 3本部门数据权限 4本部门及以下数据权限 5仅本人数据权限)")
    private String dataScope;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("状态(0正常 1停用)")
    private Integer status;

    @ApiModelProperty("菜单ID列表")
    private List<Long> menuIds;

    @ApiModelProperty("部门ID列表(数据权限)")
    private List<Long> deptIds;
}

