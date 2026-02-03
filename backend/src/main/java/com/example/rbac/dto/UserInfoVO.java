package com.example.rbac.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 用户信息响应DTO
 */
@Data
@ApiModel("用户信息")
public class UserInfoVO {

    @ApiModelProperty("用户ID")
    private Long userId;

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("昵称")
    private String nickname;

    @ApiModelProperty("头像")
    private String avatar;

    @ApiModelProperty("角色列表")
    private List<String> roles;

    @ApiModelProperty("权限列表")
    private List<String> permissions;
}
