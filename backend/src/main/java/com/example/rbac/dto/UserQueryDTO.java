package com.example.rbac.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 用户查询DTO
 */
@Data
@ApiModel("用户查询")
public class UserQueryDTO {

    @ApiModelProperty("用户账号")
    private String username;

    @ApiModelProperty("用户昵称")
    private String nickname;

    @ApiModelProperty("手机号码")
    private String phone;

    @ApiModelProperty("状态(0正常 1停用)")
    private Integer status;

    @ApiModelProperty("部门ID")
    private Long deptId;
}
