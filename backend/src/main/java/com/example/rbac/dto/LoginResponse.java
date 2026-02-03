package com.example.rbac.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 登录响应DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "登录响应")
public class LoginResponse {

    @ApiModelProperty(value = "访问令牌", required = true)
    private String token;
}
