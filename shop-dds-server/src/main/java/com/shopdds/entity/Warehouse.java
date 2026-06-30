package com.shopdds.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 仓库表 warehouse
 */
@Data
@TableName("warehouse")
public class Warehouse {

    @TableId("warehouse_Id")
    @TableField("warehouse_Id")
    private String warehouseId;

    @TableField("warehouse_Name")
    private String warehouseName;

    @TableField("warehouse_Adr")
    private String warehouseAdr;

    @TableField("warehouse_Tel")
    private String warehouseTel;

    /** 关联的仓库管理员 employee_Id */
    @TableField("employee_Id")
    private String employeeId;
}