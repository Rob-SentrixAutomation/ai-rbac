package com.example.rbac.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 用户新增DTO
 */
@Data
@ApiModel("用户新增")
public class UserAddDTO {

    @ApiModelProperty("部门ID")
    private Long deptId;

    @ApiModelProperty(value = "用户账号", required = true)
    @NotBlank(message = "用户账号不能为空")
    @Pattern(regexp = "^[a-zA-Z0-9]{4,20}$", message = "用户账号必须是4-20位字母或数字")
    private String username;

    @ApiModelProperty(value = "用户昵称", required = true)
    @NotBlank(message = "用户昵称不能为空")
    private String nickname;

    @ApiModelProperty("用户邮箱")
    @Email(message = "邮箱格式不正确")
    private String email;

    @ApiModelProperty("手机号码")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;

    @ApiModelProperty("用户性别(0男 1女 2未知)")
    private String sex;

    @ApiModelProperty("头像地址")
    private String avatar;

    @ApiModelProperty(value = "密码", required = true)
    @NotBlank(message = "密码不能为空")
    @Pattern(regexp = "^.{6,20}$", message = "密码长度必须在6-20位之间")
    private String password;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("状态(0正常 1停用)")
    private Integer status;

    @ApiModelProperty("角色ID列表")
    private java.util.List<Long> roleIds;
}

