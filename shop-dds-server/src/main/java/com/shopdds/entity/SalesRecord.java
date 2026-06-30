package com.shopdds.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 销售流水表 transaction_record
 */
@Data
@TableName("transaction_record")
public class SalesRecord {

    @TableId("transid")
    @TableField("transid")
    private String transid;

    @TableField("supermarket_id")
    private String supermarketId;

    @TableField("commodity_id")
    private String commodityId;

    @TableField("quantity")
    private Integer quantity;

    @TableField("totalPrice")
    private BigDecimal totalPrice;

    /** 可为空，关联会员 */
    @TableField("memberId")
    private String memberId;

    /** 销售时间字符串（原表存为字符串） */
    @TableField("saleTime")
    private String saleTime;
}