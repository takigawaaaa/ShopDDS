package com.shopdds.common;

import lombok.Getter;

/**
 * 系统角色枚举
 * <p>
 * 对应原系统的三类登录身份：
 * <ul>
 *   <li>HEAD_ADMIN       —— 总部管理员（原 headadmin 表）</li>
 *   <li>WAREHOUSE_ADMIN  —— 仓库管理员（原 employee 表，employee_Ide = 仓库管理员）</li>
 *   <li>SUPERMARKET_ADMIN—— 超市管理员（原 employee 表，employee_Ide = 超市管理员）</li>
 * </ul>
 */
@Getter
public enum RoleEnum {

    HEAD_ADMIN("HEAD_ADMIN", "总部管理员"),
    WAREHOUSE_ADMIN("WAREHOUSE_ADMIN", "仓库管理员"),
    SUPERMARKET_ADMIN("SUPERMARKET_ADMIN", "超市管理员");

    private final String code;
    private final String cnName;

    RoleEnum(String code, String cnName) {
        this.code = code;
        this.cnName = cnName;
    }

    /**
     * 将原系统中 employee_Ide 字段的中文身份转换为枚举
     */
    public static RoleEnum fromCnName(String cnName) {
        if (cnName == null) return null;
        return switch (cnName) {
            case "仓库管理员" -> WAREHOUSE_ADMIN;
            case "超市管理员" -> SUPERMARKET_ADMIN;
            default -> null;
        };
    }
}