package com.shopdds.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 总申请表 allapl（按商品汇总的需求量）
 */
@Data
@TableName("allapl")
public class AllApplication {

    @TableId("commodity_Id")
    @TableField("commodity_Id")
    private String commodityId;

    @TableField("allapl_Amount")
    private Integer allaplAmount;
}