package com.shopdds.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 总库存表 allinv（按商品汇总的可供量）
 */
@Data
@TableName("allinv")
public class AllInventory {

    @TableId("commodity_Id")
    @TableField("commodity_Id")
    private String commodityId;

    @TableField("allinv_Amount")
    private Integer allinvAmount;
}