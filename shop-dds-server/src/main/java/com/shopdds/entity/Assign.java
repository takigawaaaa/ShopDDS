package com.shopdds.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 物资分配表 assign
 * <p>复合主键 (supermarket_Id, commodity_Id)</p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("assign")
public class Assign {

    @TableField("supermarket_Id")
    private String supermarketId;

    @TableField("commodity_Id")
    private String commodityId;

    @TableField("assign_Amount")
    private Integer assignAmount;
}