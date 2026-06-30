package com.shopdds.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 物资调拨表 allocate
 * <p>复合主键 (supermarket_Id, warehouse_Id, commodity_Id)</p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("allocate")
public class Allocate {

    @TableField("supermarket_Id")
    private String supermarketId;

    @TableField("warehouse_Id")
    private String warehouseId;

    @TableField("commodity_Id")
    private String commodityId;

    @TableField("allocate_Amount")
    private Integer allocateAmount;
}