package com.shopdds.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 超市表 supermarket
 */
@Data
@TableName("supermarket")
public class Supermarket {

    @TableId("supermarket_Id")
    @TableField("supermarket_Id")
    private String supermarketId;

    @TableField("supermarket_Name")
    private String supermarketName;

    @TableField("supermarket_Adr")
    private String supermarketAdr;

    @TableField("supermarket_Tel")
    private String supermarketTel;

    /** 关联的超市管理员 employee_Id */
    @TableField("employee_Id")
    private String employeeId;
}