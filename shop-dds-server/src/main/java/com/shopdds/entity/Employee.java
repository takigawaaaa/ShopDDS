package com.shopdds.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 员工表 employee
 * <p>
 * employee_Ide 表示身份：仓库管理员 / 超市管理员
 */
@Data
@TableName("employee")
public class Employee {

    @TableId("employee_Id")
    @TableField("employee_Id")
    private String employeeId;

    @TableField("employee_Name")
    private String employeeName;

    /** BCrypt 哈希后的密码 */
    @TableField("employee_Pwd")
    private String employeePwd;

    /** 员工身份：仓库管理员 / 超市管理员 */
    @TableField("employee_Ide")
    private String employeeIde;

    @TableField("employee_Tel")
    private String employeeTel;
}