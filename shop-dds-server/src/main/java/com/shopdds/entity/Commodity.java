package com.shopdds.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 商品表 commodity
 */
@Data
@TableName("commodity")
public class Commodity {

    @TableId("commodity_Id")
    @TableField("commodity_Id")
    private String commodityId;

    @TableField("commodity_Barcode")
    private String commodityBarcode;

    @TableField("commodity_Name")
    private String commodityName;

    @TableField("commodity_Class")
    private String commodityClass;

    @TableField("commodity_Unit")
    private String commodityUnit;

    /** 商品单价（DECIMAL(10,2)） */
    @TableField("commodity_Price")
    private BigDecimal commodityPrice;
}