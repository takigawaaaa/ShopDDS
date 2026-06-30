package com.shopdds.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 总部管理员表 headadmin
 */
@Data
@TableName("headadmin")
public class HeadAdmin {

    @TableId("headadmin_Id")
    @TableField("headadmin_Id")
    private String headadminId;

    @TableField("headadmin_Name")
    private String headadminName;

    /** BCrypt 哈希后的密码 */
    @TableField("headadmin_Pwd")
    private String headadminPwd;

    @TableField("headadmin_Tel")
    private String headadminTel;
}