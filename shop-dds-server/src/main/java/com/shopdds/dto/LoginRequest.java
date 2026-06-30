package com.shopdds.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 登录请求
 */
@Data
public class LoginRequest {

    /** 用户编号 */
    @NotBlank(message = "用户编号不能为空")
    private String username;

    /** 密码（明文，后端比对 BCrypt） */
    @NotBlank(message = "密码不能为空")
    private String password;

    /** 角色：HEAD_ADMIN / WAREHOUSE_ADMIN / SUPERMARKET_ADMIN */
    @NotBlank(message = "角色不能为空")
    private String role;
}