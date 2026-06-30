package com.shopdds.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 各仓库库存表 inv
 * <p>复合主键 (commodity_Id, warehouse_Id)</p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("inv")
public class Inventory {

    @TableField("commodity_Id")
    private String commodityId;

    @TableField("warehouse_Id")
    private String warehouseId;

    @TableField("inv_Amount")
    private Integer invAmount;

    @TableField("inv_Time")
    private String invTime;
}