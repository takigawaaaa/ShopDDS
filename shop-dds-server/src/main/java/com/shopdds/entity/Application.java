package com.shopdds.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 各超市申请表 apl
 * <p>复合主键 (supermarket_Id, commodity_Id)</p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("apl")
public class Application {

    @TableField("supermarket_Id")
    private String supermarketId;

    @TableField("commodity_Id")
    private String commodityId;

    @TableField("apl_Amount")
    private Integer aplAmount;

    @TableField("apl_Time")
    private String aplTime;
}