package com.shopdds.dto;

import lombok.Data;

/**
 * 登录成功响应
 */
@Data
public class LoginResponse {

    private String token;
    private String userId;
    private String name;
    private String role;
    private String facilityId;
    private String facilityName;
}