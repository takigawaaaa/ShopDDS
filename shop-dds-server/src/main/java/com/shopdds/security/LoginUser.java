package com.shopdds.security;

import com.shopdds.common.RoleEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * 登录用户信息（承载于 JWT/SecurityContext 中）
 */
@Data
@Builder
@AllArgsConstructor
public class LoginUser {

    /** 用户编号（headadmin_Id 或 employee_Id） */
    private String userId;

    /** 用户姓名 */
    private String name;

    /** 角色枚举 */
    private RoleEnum role;

    /** 角色关联的机构编号（仓库或超市编号，HEAD_ADMIN 为 null） */
    private String facilityId;

    /** 角色关联的机构名称（仓库名或超市名） */
    private String facilityName;
}